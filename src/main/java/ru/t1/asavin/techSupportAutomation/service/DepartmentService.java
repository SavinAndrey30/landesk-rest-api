package ru.t1.asavin.techSupportAutomation.service;

import ru.t1.asavin.techSupportAutomation.dto.DepartmentDto;
import ru.t1.asavin.techSupportAutomation.entity.Department;

import java.util.List;


public interface DepartmentService {

    List<DepartmentDto> findAll();

    DepartmentDto update(Department department);

    DepartmentDto save(Department department);

    DepartmentDto delete(Long id);
}
