package ru.t1.asavin.techSupportAutomation.dto;

import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Department;

@Data
public class DepartmentDto {

    private Long id;
    private String name;
    private String description;

    public DepartmentDto(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
    }
}
