package ru.t1.asavin.techSupportAutomation.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.t1.asavin.techSupportAutomation.dto.UserDto;
import ru.t1.asavin.techSupportAutomation.entity.User;

public interface UserService extends UserDetailsService {

    UserDto saveUser(User user);

    UserDto addRoleToUser(Long userId, int roleId);

    UserDto delete(Long id);
}
