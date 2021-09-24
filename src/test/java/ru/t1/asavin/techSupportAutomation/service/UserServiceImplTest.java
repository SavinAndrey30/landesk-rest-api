package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.t1.asavin.techSupportAutomation.dao.RoleRepository;
import ru.t1.asavin.techSupportAutomation.dao.UserRepository;
import ru.t1.asavin.techSupportAutomation.dto.UserDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.entity.User;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;
import ru.t1.asavin.techSupportAutomation.service.util.ServiceTestsUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Long ID = 1L;
    private static final String USERNAME = "andrey";
    private static final String PASSWORD = "pass123";
    private static final String USER_FULL_NAME = "Савин АП";
    private static final String USER_PHONE = "+7222222222";
    private static final String USER_LOCATION = "Moscow";
    private static final String USER_EMAIL = "and@landesk.com";
    private static final Set<Role> ROLES = new HashSet<>(List.of(new Role("ROLE_ADMIN")));

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUser() {
        User user = ServiceTestsUtil.createUserObject(ID, USERNAME, PASSWORD, USER_FULL_NAME, USER_PHONE, USER_LOCATION, USER_EMAIL, ROLES);
        UserDto userDto = new UserDto(user);

        doReturn(user).when(userRepository).save(user);

        UserDto savedUserDto = userService.saveUser(user);

        assertEquals(savedUserDto, userDto);
        assertNotNull(userRepository.findById(savedUserDto.getId()));
    }


    @Test
    void saveUser_shouldThrowEntityAlreadyExistsException() {
        User user = ServiceTestsUtil.createUserObject(ID, USERNAME, PASSWORD, USER_FULL_NAME, USER_PHONE, USER_LOCATION, USER_EMAIL, ROLES);

        doReturn(true).when(userRepository).existsUserByUsername(USERNAME);

        assertThrows(EntityAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    void addRoleToUser() {
        User user = ServiceTestsUtil.createUserObject(ID, USERNAME, PASSWORD, USER_FULL_NAME, USER_PHONE, USER_LOCATION, USER_EMAIL, ROLES);
        Role role = new Role(2, "ROLE_ANALYST");

        doReturn(Optional.of(role)).when(roleRepository).findById(2);
        doReturn(Optional.of(user)).when(userRepository).findById(ID);
        doReturn(user).when(userRepository).save(user);

        UserDto savedUserDto = userService.addRoleToUser(ID, 2);

        assertEquals(savedUserDto.getRoles().size(), 2);
    }

    @Test
    void delete() {
        User userToDelete = ServiceTestsUtil.createUserObject(ID, USERNAME, PASSWORD, USER_FULL_NAME, USER_PHONE, USER_LOCATION, USER_EMAIL, ROLES);
        UserDto userToDeleteDto = new UserDto(userToDelete);

        doReturn(Optional.of(userToDelete)).when(userRepository).findById(ID);

        UserDto deletedUserDto = userService.delete(ID);

        assertThat(userRepository.existsById(ID)).isFalse();
        assertEquals(userToDeleteDto, deletedUserDto);
    }
}