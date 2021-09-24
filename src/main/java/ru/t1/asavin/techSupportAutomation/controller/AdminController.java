package ru.t1.asavin.techSupportAutomation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.asavin.techSupportAutomation.dto.RoleDto;
import ru.t1.asavin.techSupportAutomation.dto.UserDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.entity.User;
import ru.t1.asavin.techSupportAutomation.service.RoleService;
import ru.t1.asavin.techSupportAutomation.service.UserService;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/users")
    public UserDto createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/roles")
    public RoleDto createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @DeleteMapping("/users/{userId}")
    public UserDto deleteUser(@PathVariable Long userId) {
        return userService.delete(userId);
    }

    @PatchMapping("/users/{userId}/roles/{roleId}")
    public UserDto addRoleToUser(@PathVariable Long userId, @PathVariable int roleId) {
        return userService.addRoleToUser(userId, roleId);
    }
}
