package ro.mdx.meditation.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ro.mdx.meditation.dto.TeacherDto;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.model.Teacher;
import ro.mdx.meditation.repository.UserRepository;

@Mapper(componentModel = "spring")
public abstract class TeacherMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "username", target = "user", qualifiedByName = "mapUsernameToUser")
    public abstract Teacher toEntity(TeacherDto dto);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.id", target = "userId")
    public abstract TeacherDto toDto(Teacher teacher);

    @Named("mapUsernameToUser")
    protected AppUser mapUsernameToUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
