package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.asavin.techSupportAutomation.dao.DepartmentRepository;
import ru.t1.asavin.techSupportAutomation.dto.DepartmentDto;
import ru.t1.asavin.techSupportAutomation.entity.Department;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "НИЦ";
    private static final String DESCRIPTION = "Научно-исследовательский отдел";

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void findAll() {
        Department department = createDepartmentObject(ID, NAME, DESCRIPTION);
        List<Department> departments = List.of(department);
        List<DepartmentDto> actualDepartments = List.of(new DepartmentDto(department));

        doReturn(departments).when(departmentRepository).findAll();

        List<DepartmentDto> expectedDepartments = departmentService.findAll();

        assertThat(expectedDepartments).isEqualTo(actualDepartments);
    }

    @Test
    void update() {
        Department department = createDepartmentObject(ID, NAME, DESCRIPTION);

        doReturn(true).when(departmentRepository).existsById(ID);
        doReturn(department).when(departmentRepository).save(department);

        DepartmentDto updatedDepartmentDto = departmentService.update(department);
        assertThat(updatedDepartmentDto).isNotNull();

        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void save() {
        Department department = createDepartmentObject(ID, NAME, DESCRIPTION);
        DepartmentDto departmentDto = new DepartmentDto(department);

        doReturn(department).when(departmentRepository).save(department);

        DepartmentDto savedDepartmentDto = departmentService.save(department);

        assertEquals(savedDepartmentDto, departmentDto);
        assertNotNull(departmentRepository.findById(savedDepartmentDto.getId()));
    }

    @Test
    void save_shouldThrowEntityAlreadyExistsException() {
        Department department = createDepartmentObject(ID, NAME, DESCRIPTION);

        doReturn(true).when(departmentRepository).existsByName(NAME);

        assertThrows(EntityAlreadyExistsException.class, () -> departmentService.save(department));
    }

    @Test
    void delete() {
        Department departmentToDelete = createDepartmentObject(ID, NAME, DESCRIPTION);
        DepartmentDto departmentToDeleteDto = new DepartmentDto(departmentToDelete);

        doReturn(Optional.of(departmentToDelete)).when(departmentRepository).findById(ID);

        DepartmentDto deletedDepartmentDto = departmentService.delete(ID);
        assertThat(departmentRepository.existsById(ID)).isFalse();
        assertEquals(departmentToDeleteDto, deletedDepartmentDto);
    }

    public Department createDepartmentObject(Long id, String name, String description) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setDescription(description);

        return department;
    }
}