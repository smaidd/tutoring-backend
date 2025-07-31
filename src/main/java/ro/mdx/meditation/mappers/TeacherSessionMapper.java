package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.TeacherSessionDto;
import ro.mdx.meditation.model.TeacherSession;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class, StudentMapper.class, SubjectMapper.class})
public interface TeacherSessionMapper extends EntityMapper<TeacherSession, TeacherSessionDto> {
}
