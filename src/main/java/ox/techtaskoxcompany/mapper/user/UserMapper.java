package ox.techtaskoxcompany.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ox.techtaskoxcompany.config.MapperConfig;
import ox.techtaskoxcompany.dto.user.UserRegistrationRequest;
import ox.techtaskoxcompany.dto.user.UserResponseDto;
import ox.techtaskoxcompany.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequest request);
}
