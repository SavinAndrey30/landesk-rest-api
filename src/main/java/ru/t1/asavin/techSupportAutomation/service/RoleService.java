package ru.t1.asavin.techSupportAutomation.service;

import ru.t1.asavin.techSupportAutomation.dto.RoleDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;


public interface RoleService {

    RoleDto saveRole(Role role);
}
