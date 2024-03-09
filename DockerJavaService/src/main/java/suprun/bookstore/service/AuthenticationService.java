package suprun.bookstore.service;

import suprun.bookstore.dto.user.UserLoginRequestDto;
import suprun.bookstore.dto.user.UserLoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
