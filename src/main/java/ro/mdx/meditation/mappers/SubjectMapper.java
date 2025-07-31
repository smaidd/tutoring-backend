package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.SubjectDto;
import ro.mdx.meditation.model.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDto toDto(Subject entity);

    Subject toEntity(SubjectDto dto);
}
