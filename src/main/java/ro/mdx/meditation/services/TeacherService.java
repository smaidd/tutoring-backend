package ro.mdx.meditation.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.dto.PresenceStudentDto;
import ro.mdx.meditation.dto.SubjectDto;
import ro.mdx.meditation.dto.TeacherDto;
import ro.mdx.meditation.dto.TeacherSessionDto;
import ro.mdx.meditation.exceptions.teacher.TeacherNotFoundException;
import ro.mdx.meditation.mappers.StudentMapper;
import ro.mdx.meditation.mappers.SubjectMapper;
import ro.mdx.meditation.mappers.TeacherMapper;
import ro.mdx.meditation.mappers.TeacherSessionMapper;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.model.Student;
import ro.mdx.meditation.model.Teacher;
import ro.mdx.meditation.model.TeacherRemuneration;
import ro.mdx.meditation.model.TeacherSession;
import ro.mdx.meditation.repository.RemunerationRepository;
import ro.mdx.meditation.repository.TeacherRepository;
import ro.mdx.meditation.repository.TeacherSessionRepository;
import ro.mdx.meditation.utils.AuthorizationService;
import ro.mdx.meditation.utils.Role;
import ro.mdx.meditation.utils.UserUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;
    private final RemunerationRepository remunerationRepository;
    private final AuthorizationService authorizationService;
    private final TeacherSessionRepository teacherSessionRepository;
    private final TeacherSessionMapper teacherSessionMapper;
    private final SubjectMapper subjectMapper;

    @Transactional
    public TeacherDto createTeacher(TeacherDto teacher) {
        Teacher entity = teacherMapper.toEntity(teacher);

        AppUser user = new AppUser();
        user.setEmail(teacher.email());
        user.setRole(Role.PROFESSOR);
        user.setUsername(teacher.username());
        user.setPassword(UserUtils.generateRandomPassword());

        AppUser savedUser = userService.createUser(user);
        entity.setUser(savedUser);

        Teacher save = teacherRepository.save(entity);

        return teacherMapper.toDto(save);
    }

    public ElementPage<TeacherDto> getTeachers(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<TeacherDto> studentDtoPage = teacherRepository.findAll(pageRequest).map(teacherMapper::toDto);
        return new ElementPage<>(studentDtoPage.getContent(), studentDtoPage.getTotalPages(), studentDtoPage.getTotalElements());
    }

    public TeacherDto getTeacherByUserId(UUID userId) {
        return teacherRepository.findByUserId(userId).map(teacherMapper::toDto).orElse(null);
    }

    public void deleteTeacher(UUID teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    public TeacherDto getTeacher(UUID teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(TeacherNotFoundException::new);
        return teacherMapper.toDto(teacher);
    }

    public void saveTeacherSession(TeacherDto professor, List<PresenceStudentDto> students, LocalDateTime classDate, SubjectDto subject) {
        List<Student> sessionStudents = students.stream()
                .map(PresenceStudentDto::getStudent)
                .map(studentMapper::toEntity)
                .toList();

        TeacherSession teacherSession = new TeacherSession();
        teacherSession.setTeacher(teacherMapper.toEntity(professor));
        teacherSession.setClassDate(classDate);
        teacherSession.setStudents(sessionStudents);
        teacherSession.setRemuneration(calculateTeacherRemuneration(students));
        teacherSession.setSubject(subjectMapper.toEntity(subject));

        teacherSessionRepository.save(teacherSession);
    }

    private double calculateTeacherRemuneration(List<PresenceStudentDto> students) {
        if (authorizationService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            return 0;
        }
        int numberOfStudents = students.size();

        // assuming only one active rule; adjust if multiple exist
        Optional<TeacherRemuneration> ruleOpt = remunerationRepository.findAll().stream().findFirst();

        if (ruleOpt.isEmpty()) {
            throw new IllegalStateException("No remuneration rule configured.");
        }

        TeacherRemuneration rule = ruleOpt.get();

        int base = rule.getBaseStudents();
        BigDecimal baseAmount = rule.getBaseAmount();
        int step = rule.getStepStudents();
        BigDecimal stepAmount = rule.getStepAmount();

        if (numberOfStudents <= base) {
            return baseAmount.doubleValue();
        }

        int steps = (numberOfStudents - base) / step;
        BigDecimal total = baseAmount.add(stepAmount.multiply(BigDecimal.valueOf(steps)));

        return total.doubleValue();
    }

    public List<TeacherSessionDto> getTeacherClasses(UUID teacherId, String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        List<TeacherSession> sessions = teacherSessionRepository.findTeacherSessions(teacherId, fromDate, toDate);
        return sessions.stream().map(teacherSessionMapper::toDto).toList();
    }
}
