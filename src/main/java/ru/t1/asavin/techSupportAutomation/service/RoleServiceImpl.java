package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.RoleRepository;
import ru.t1.asavin.techSupportAutomation.dto.RoleDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto saveRole(Role role) {
        String roleName = role.getName();

        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
            role.setName(roleName);
        }

        if (!roleRepository.existsByName(roleName)) {
            log.info("Saving role with the name " + roleName);
            return new RoleDto(roleRepository.save(role));
        } else {
            log.error("Saving role: role with the name " + roleName + " already exists");
            throw new EntityAlreadyExistsException("Role with the name " + roleName + " already exists");
        }
    }
}
