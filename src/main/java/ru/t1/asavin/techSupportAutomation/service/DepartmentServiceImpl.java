package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.DepartmentRepository;
import ru.t1.asavin.techSupportAutomation.dto.DepartmentDto;
import ru.t1.asavin.techSupportAutomation.entity.Department;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> findAll() {
        return departmentRepository.findAll().stream().map(DepartmentDto::new).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto update(Department department) {
        Long departmentId = department.getId();
        if (departmentRepository.existsById(departmentId)) {
            log.info("Updating department with the id " + departmentId);
            return new DepartmentDto(departmentRepository.save(department));
        } else {
            log.error("Updating department: department with the id " + departmentId + " is not found");
            throw new EntityNotFoundException("Department with the id " + departmentId + " is not found");
        }
    }

    @Override
    public DepartmentDto save(Department department) {
        String departmentName = department.getName();
        if (!departmentRepository.existsByName(departmentName)) {
            log.info("Saving department with the name " + departmentName);
            return new DepartmentDto(departmentRepository.save(department));
        } else {
            log.error("Saving department: department " + departmentName + " already exists");
            throw new EntityAlreadyExistsException("Department " + departmentName + " already exists");
        }
    }

    @Override
    public DepartmentDto delete(Long id) {
        return departmentRepository.findById(id)
                .map(department -> {
                    departmentRepository.deleteById(id);
                    log.info("Department with the id " + id + " is deleted");
                    return new DepartmentDto(department);
                })
                .orElseThrow(() -> {
                    log.error("Deleting department: department with the id " + id + " is not found");
                    return new EntityNotFoundException("department with the id " + id + " is not found");
                });
    }
}
