package ox.techtaskoxcompany.service.user;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ox.techtaskoxcompany.dto.user.UserRegistrationRequest;
import ox.techtaskoxcompany.dto.user.UserResponseDto;
import ox.techtaskoxcompany.exception.RegistrationException;
import ox.techtaskoxcompany.mapper.user.UserMapper;
import ox.techtaskoxcompany.model.Role;
import ox.techtaskoxcompany.model.Role.RoleName;
import ox.techtaskoxcompany.model.User;
import ox.techtaskoxcompany.repository.user.UserRepository;
import ox.techtaskoxcompany.service.role.RoleService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserResponseDto register(UserRegistrationRequest request) throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleService.getRoleByRoleName(RoleName.USER);
        user.setRoles(new HashSet<>(Set.of(userRole)));
        return userMapper.toDto(userRepository.save(user));
    }
}
