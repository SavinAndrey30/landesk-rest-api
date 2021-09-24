package ru.t1.asavin.techSupportAutomation.dto;

import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Role;

@Data
public class RoleDto {

    private int id;
    private String name;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}