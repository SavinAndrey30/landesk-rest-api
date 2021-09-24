package ru.t1.asavin.techSupportAutomation.dto;

import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String userFullName;
    private String userPhone;
    private String userLocation;
    private String userEmail;
    private Set<RoleDto> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userFullName = user.getUserFullName();
        this.userPhone = user.getUserPhone();
        this.userLocation = user.getUserLocation();
        this.userEmail = user.getUserEmail();
        this.roles = getRolesDto(user);
    }

    private Set<RoleDto> getRolesDto(User user) {
        Set<Role> roles = user.getRoles();
        if (!(roles == null)){
            return roles
                    .stream()
                    .map(RoleDto::new)
                    .collect(Collectors.toSet());
        }
        return null;
    }
}
