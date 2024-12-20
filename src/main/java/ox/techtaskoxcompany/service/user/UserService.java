package ox.techtaskoxcompany.service.user;

import ox.techtaskoxcompany.dto.user.UserRegistrationRequest;
import ox.techtaskoxcompany.dto.user.UserResponseDto;
import ox.techtaskoxcompany.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest request) throws RegistrationException;
}
