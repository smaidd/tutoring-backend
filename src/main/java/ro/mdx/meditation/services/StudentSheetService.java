package ro.mdx.meditation.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.dto.PresenceDto;
import ro.mdx.meditation.dto.PresenceStudentDto;
import ro.mdx.meditation.dto.StudentSheetDto;
import ro.mdx.meditation.dto.StudentSheetStatistics;
import ro.mdx.meditation.dto.TeacherDto;
import ro.mdx.meditation.exceptions.sheet.SheetNotFoundException;
import ro.mdx.meditation.exceptions.student.StudentNotFoundException;
import ro.mdx.meditation.exceptions.teacher.TeacherNotFoundException;
import ro.mdx.meditation.http.request.PresentSheetCreationRequest;
import ro.mdx.meditation.mappers.PresenceMapper;
import ro.mdx.meditation.mappers.StudentSheetMapper;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.model.Presence;
import ro.mdx.meditation.model.Student;
import ro.mdx.meditation.model.StudentSheet;
import ro.mdx.meditation.model.TeacherRemuneration;
import ro.mdx.meditation.model.TeacherSession;
import ro.mdx.meditation.repository.RemunerationRepository;
import ro.mdx.meditation.repository.StudentRepository;
import ro.mdx.meditation.repository.StudentSheetRepository;
import ro.mdx.meditation.utils.AuthorizationService;
import ro.mdx.meditation.utils.Role;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class StudentSheetService {
    private final StudentRepository studentRepository;
    private final StudentSheetRepository studentSheetRepository;
    private final StudentSheetMapper studentSheetMapper;
    private final PresenceMapper presenceMapper;
    private final StudentService studentService;
    private final AuthorizationService authorizationService;
    private final TeacherService teacherService;

    public void saveStudentSheet(String studentId) {
        Optional<Student> byId = studentRepository.findById(UUID.fromString(studentId));
        if (byId.isEmpty()) {
            log.error("Student with id {} not found", studentId);
            throw new StudentNotFoundException();
        }

        StudentSheet studentSheet = new StudentSheet();
        studentSheet.setStudent(byId.get());

        studentSheetRepository.save(studentSheet);
    }


    public ElementPage<StudentSheetDto> getStudentSheets(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<StudentSheet> page;
        if (authorizationService.isAdmin()) {
            page = studentSheetRepository.findAll(pageRequest);
        } else {
            TeacherDto teacher = teacherService.getTeacherByUserId(authorizationService.getCurrentUser().getId());
            if (teacher == null) {
                throw new TeacherNotFoundException();
            }
            page = studentSheetRepository.findAllByTeacher(UUID.fromString(teacher.id()), pageRequest);
        }

        Page<StudentSheetDto> studentDtoPage = page.map(studentSheetMapper::toDto);
        return new ElementPage<>(studentDtoPage.getContent(), studentDtoPage.getTotalPages(), studentDtoPage.getTotalElements());
    }

    public StudentSheetDto getStudentSheet(UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
        Optional<StudentSheet> byStudentId = studentSheetRepository.findByStudentId(student.getId());
        StudentSheetDto studentSheetDto = byStudentId.map(studentSheetMapper::toDto)
                .orElseThrow(SheetNotFoundException::new);
        AppUser currentUser = authorizationService.getCurrentUser();

        if (!Role.ADMIN.equals(currentUser.getRole())) {
            List<PresenceDto> filteredPresence = studentSheetDto.presence().stream()
                    .filter(p -> Objects.nonNull(p.professor()))
                    .filter(p -> currentUser.getId().toString().equals(p.professor().userId()))
                    .toList();

            studentSheetDto = new StudentSheetDto(
                    studentSheetDto.id(),
                    studentSheetDto.student(),
                    filteredPresence
            );
        }

        return studentSheetDto;
    }

    @Transactional
    public void addNewStudentPresence(PresenceDto presence, UUID studentId) {
        saveStudentPresence(presence, studentId);
        studentService.updateNumberOfSessions(studentId);
    }

    private void saveStudentPresence(PresenceDto presence, UUID studentId) {
        Optional<StudentSheet> optionalStudentSheet = studentSheetRepository.findByStudentId(studentId);

        StudentSheet studentSheet = optionalStudentSheet.orElseThrow(StudentNotFoundException::new);

        studentSheet.getPresence().add(presenceMapper.toEntity(presence));
        studentSheetRepository.save(studentSheet);
    }

    @Transactional
    public void createPresenceSheet(PresentSheetCreationRequest presentSheetCreationRequest) {
        AppUser currentUser = authorizationService.getCurrentUser();
        TeacherDto professor;
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            TeacherDto teacherByUserId = teacherService.getTeacherByUserId(currentUser.getId());
            if (teacherByUserId == null) {
                throw new TeacherNotFoundException();
            }
            professor = teacherByUserId;
        } else {
            professor = null;
        }

        presentSheetCreationRequest.students().forEach(student -> {
            Optional<StudentSheet> optionalStudentSheet = studentSheetRepository.findByStudentId(UUID.fromString(student.getStudent().id()));
            StudentSheet studentSheet = optionalStudentSheet.orElseThrow(StudentNotFoundException::new);


            PresenceDto presenceDto = new PresenceDto(null, presentSheetCreationRequest.classDate(), presentSheetCreationRequest.subject(),
                    professor, student.isPresent(), student.getDelayMinutes(), student.getHomeworkPercentage(), student.getParticipationPercentage(), student.getNotes());

            studentSheet.getPresence().add(presenceMapper.toEntity(presenceDto));
            studentSheetRepository.save(studentSheet);
            studentService.updateNumberOfSessions(studentSheet.getStudent().getId());
        });

        teacherService.saveTeacherSession(professor,  presentSheetCreationRequest.students(), presentSheetCreationRequest.classDate() ,presentSheetCreationRequest.subject());
    }

    public StudentSheetStatistics getStudentSheetStatistics(UUID sheetId) {
        StudentSheet studentSheet = studentSheetRepository.findById(sheetId).orElseThrow(SheetNotFoundException::new);


        AppUser currentUser = authorizationService.getCurrentUser();
        if (!Role.ADMIN.equals(currentUser.getRole())) {
            studentSheet.setPresence(
                    studentSheet.getPresence()
                            .stream()
                            .filter(p -> Objects.nonNull(p.getProfessor()))
                            .filter(p -> p.getProfessor().getUser().getId().equals(currentUser.getId()))
                            .toList()
            );
        }
        int totalNumberOfSessions = studentSheet.getPresence().size();
        double presencePercentage = getPresencePercentage(studentSheet.getPresence());
        double medianActiveParticipation = getMedianActiveParticipation(studentSheet.getPresence());
        double medianHomework = getMedianHomework(studentSheet.getPresence());

        return new StudentSheetStatistics(totalNumberOfSessions,
                presencePercentage,
                medianActiveParticipation,
                medianHomework);
    }

    private double getPresencePercentage(List<Presence> presences) {
        if (presences.isEmpty()) {
            return 0;
        }

        double presenceCount = presences.stream()
                .filter(Presence::isPresent)
                .count();
        double total = presences.size();
        return round1Decimal((presenceCount / total) * 100.0);
    }

    private double getMedianActiveParticipation(List<Presence> presences) {
        List<Integer> scores = presences.stream()
                .filter(Presence::isPresent)
                .map(Presence::getParticipationPercentage)
                .sorted()
                .toList();

        int n = scores.size();
        if (n == 0) {
            return 0;
        }

        return round1Decimal(scores.stream().reduce(0, Integer::sum) / (double) n);
    }

    private double getMedianHomework(List<Presence> presences) {
        List<Integer> scores = presences.stream()
                .filter(Presence::isPresent)
                .map(Presence::getHomeworkPercentage)
                .sorted()
                .toList();

        int n = scores.size();
        if (n == 0) {
            return 0;
        }

        double median = scores.stream().reduce(0, Integer::sum) / (double) n;

        return round1Decimal(median);
    }

    private double round1Decimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

}
