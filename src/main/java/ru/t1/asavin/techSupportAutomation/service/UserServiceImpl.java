package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.RoleRepository;
import ru.t1.asavin.techSupportAutomation.dao.UserRepository;
import ru.t1.asavin.techSupportAutomation.dto.UserDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.entity.User;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto saveUser(User user) {
        String username = user.getUsername();
        if (!userRepository.existsUserByUsername(username)) {
            log.info("Saving user with the username " + username);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new UserDto(userRepository.save(user));
        } else {
            log.error("Saving new user: user with the name " + username + " already exists");
            throw new EntityAlreadyExistsException("user with the name " + username + " already exists");
        }
    }

    @Override
    public UserDto addRoleToUser(Long userId, int roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        return userRepository.findById(userId)
                .map(user -> {
                    if (roleOptional.isPresent()) {
                        Role role = roleOptional.get();
                        user.getRoles().add(role);
                        log.info("Role " + role.getName() + " is assigned to the user " + user.getUsername());
                    } else {
                        log.error("Adding role to the user: role with the id " + roleId + " is not found");
                        throw new EntityNotFoundException("Role with the id " + roleId + " is not found");
                    }

                    return new UserDto(userRepository.save(user));
                })
                .orElseThrow(() -> {
                    log.error("Adding role to the user: user with the id " + userId + " is not found");
                    return new EntityNotFoundException("User with the id " + userId + " is not found");
                });
    }

    @Override
    public UserDto delete(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    log.info("User with the id " + id + " is deleted");
                    return new UserDto(user);
                })
                .orElseThrow(() -> {
                    log.error("Deleting user: user with the id " + id + " is not found");
                    return new EntityNotFoundException("user with the id " + id + " is not found");
                });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(User::toSpringUser)
                .orElseThrow(() -> {
                    log.error("Loading user by username: invalid username or password");
                    return new UsernameNotFoundException("Invalid username or password");
                });
    }
}
