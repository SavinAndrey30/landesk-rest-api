package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.asavin.techSupportAutomation.dao.RoleRepository;
import ru.t1.asavin.techSupportAutomation.dto.RoleDto;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private static final int ID = 1;
    private static final String NAME = "ROLE_ADMIN";

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void saveRole() {
        Role role = createRoleObject(ID, NAME);
        RoleDto roleDto = new RoleDto(role);

        doReturn(role).when(roleRepository).save(role);

        RoleDto savedRoleDto = roleService.saveRole(role);

        assertEquals(savedRoleDto, roleDto);
        assertNotNull(roleRepository.findById(savedRoleDto.getId()));
    }

    @Test
    void save_shouldThrowEntityAlreadyExistsException() {
        Role role = createRoleObject(ID, NAME);

        doReturn(true).when(roleRepository).existsByName(NAME);

        assertThrows(EntityAlreadyExistsException.class, () -> roleService.saveRole(role));
    }

    public Role createRoleObject(int id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        return role;
    }
}