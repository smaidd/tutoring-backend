package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.StudentSheetDto;
import ro.mdx.meditation.model.StudentSheet;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class, StudentMapper.class})
public interface StudentSheetMapper {

    StudentSheetDto toDto(StudentSheet entity);

    StudentSheet toEntity(StudentSheetDto dto);
}
