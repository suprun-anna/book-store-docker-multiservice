package suprun.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import suprun.bookstore.dto.user.UserLoginRequestDto;
import suprun.bookstore.dto.user.UserLoginResponseDto;
import suprun.bookstore.security.JwtUtil;
import suprun.bookstore.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtUtil.generateToken(request.email());
        return new UserLoginResponseDto(token);
    }
}
