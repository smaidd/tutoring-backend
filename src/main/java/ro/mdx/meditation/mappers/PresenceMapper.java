package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import ro.mdx.meditation.dto.PresenceDto;
import ro.mdx.meditation.model.Presence;

@Mapper(componentModel = "spring")
public interface PresenceMapper {

    PresenceDto toDto(Presence entity);

    Presence toEntity(PresenceDto dto);
}
