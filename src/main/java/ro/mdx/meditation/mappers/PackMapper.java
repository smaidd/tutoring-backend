package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.PackDto;
import ro.mdx.meditation.model.Pack;

@Mapper(componentModel = "spring")
public interface PackMapper {

    PackDto toDto(Pack entity);

    Pack toEntity(PackDto dto);
}
