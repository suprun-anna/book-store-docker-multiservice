package suprun.bookstore.mapper;

import suprun.bookstore.config.MapperConfig;
import suprun.bookstore.dto.user.UserRegistrationRequestDto;
import suprun.bookstore.dto.user.UserResponseDto;
import suprun.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto bookDto);
}
