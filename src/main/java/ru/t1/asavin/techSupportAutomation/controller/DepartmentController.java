package ru.t1.asavin.techSupportAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.asavin.techSupportAutomation.dto.DepartmentDto;
import ru.t1.asavin.techSupportAutomation.entity.Department;
import ru.t1.asavin.techSupportAutomation.service.DepartmentService;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public List<DepartmentDto> getDepartments() {
        return departmentService.findAll();
    }

    @PostMapping("/departments")
    public DepartmentDto createDepartment(@RequestBody Department department) {
        return departmentService.save(department);
    }

    @PutMapping("/departments")
    public DepartmentDto updateDepartment(@RequestBody Department department) {
        return departmentService.update(department);
    }

    @DeleteMapping("/departments/{departmentId}")
    public DepartmentDto deleteDepartment(@PathVariable Long departmentId) {
        return departmentService.delete(departmentId);
    }
}
