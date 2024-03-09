package suprun.bookstore.service;

import suprun.bookstore.dto.user.UserRegistrationRequestDto;
import suprun.bookstore.dto.user.UserResponseDto;
import suprun.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userDto);

    UserResponseDto getByEmail(String email);

    User getById(Long id);
}
