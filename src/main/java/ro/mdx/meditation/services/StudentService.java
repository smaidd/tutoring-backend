package ro.mdx.meditation.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.dto.StudentDto;
import ro.mdx.meditation.exceptions.student.StudentNotFoundException;
import ro.mdx.meditation.http.StudentFilters;
import ro.mdx.meditation.mappers.StudentMapper;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.model.Student;
import ro.mdx.meditation.repository.StudentRepository;
import ro.mdx.meditation.repository.StudentSheetRepository;
import ro.mdx.meditation.repository.specs.SpecsUtil;
import ro.mdx.meditation.repository.specs.StudentSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentSheetRepository studentSheetRepository;
    private final StudentMapper studentMapper;

    public StudentDto saveStudent(StudentDto studentDto) {
        Student entity = studentMapper.toEntity(studentDto);
        entity.setNumberOfSessions(studentDto.pack().numberOfSessions());
        Student savedStudent = studentRepository.save(entity);

        return studentMapper.toDto(savedStudent);
    }

    public ElementPage<StudentDto> getStudents(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<StudentDto> studentDtoPage = studentRepository.findAll(pageRequest).map(studentMapper::toDto);
        return new ElementPage<>(studentDtoPage.getContent(), studentDtoPage.getTotalPages(), studentDtoPage.getTotalElements());
    }

    public StudentDto getStudentById(UUID studentId) {
        Optional<Student> byId = studentRepository.findById(studentId);

        return byId.map(studentMapper::toDto).orElseThrow(StudentNotFoundException::new);
    }

    public StudentDto updateStudent(UUID studentId, StudentDto studentDto) {
        if (!studentId.toString().equals(studentDto.id())) {
            throw new StudentNotFoundException();
        }
        Student updatedStudent = studentRepository.save(studentMapper.toEntity(studentDto));
        return studentMapper.toDto(updatedStudent);
    }

    public void updateNumberOfSessions(UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

        student.setNumberOfSessions(student.getNumberOfSessions() - 1);

        studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        studentSheetRepository.findByStudentId(studentId).ifPresent(studentSheetRepository::delete);
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public List<StudentDto> getStudentsByFilters(StudentFilters studentFilters) {
        List<Specification<Student>> specs = new ArrayList<>();

        studentFilters.getSubject().ifPresent(subject -> specs.add(StudentSpecifications.hasSubjectId(subject)));

        Specification<Student> studentSpecification = SpecsUtil.combineSpecifications(specs);
        List<Student> students = studentRepository.findAll(studentSpecification);
        return students.stream().map(studentMapper::toDto).toList();
    }
}
