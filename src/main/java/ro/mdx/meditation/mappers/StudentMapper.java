package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.StudentDto;
import ro.mdx.meditation.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto toDto(Student entity);

    Student toEntity(StudentDto dto);
}