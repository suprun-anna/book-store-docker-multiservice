package suprun.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import suprun.bookstore.dto.user.UserRegistrationRequestDto;
import suprun.bookstore.dto.user.UserResponseDto;
import suprun.bookstore.exception.RegistrationException;
import suprun.bookstore.mapper.UserMapper;
import suprun.bookstore.model.Role;
import suprun.bookstore.model.RoleName;
import suprun.bookstore.model.User;
import suprun.bookstore.repository.role.RoleRepository;
import suprun.bookstore.repository.user.UserRepository;
import suprun.bookstore.service.ShoppingCartService;
import suprun.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final RoleName DEFAULT_ROLE_NAME = RoleName.ROLE_USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userDto) {
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new RegistrationException("Can't register user with email "
                    + userDto.email());
        }
        User user = userMapper.toModel(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Set<Role> userRoles = new HashSet<>();
        Role userRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new RegistrationException("Default user role not found"));
        userRoles.add(userRole);
        user.setRoles(userRoles);
        user = userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by email " + email));
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id= " + id));
    }
}
