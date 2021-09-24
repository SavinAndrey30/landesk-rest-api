package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.PriorityRepository;
import ru.t1.asavin.techSupportAutomation.dto.PriorityDto;
import ru.t1.asavin.techSupportAutomation.entity.Priority;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;

    @Override
    public List<PriorityDto> findAll() {
        return priorityRepository.findAll().stream().map(PriorityDto::new).collect(Collectors.toList());
    }

    @Override
    public PriorityDto update(Priority priority) {
        int priorityId = priority.getId();
        if (priorityRepository.existsById(priorityId)) {
            log.info("Updating priority with the id " + priorityId);
            return new PriorityDto(priorityRepository.save(priority));
        } else {
            log.error("Updating priority: priority with the id " + priorityId + " is not found");
            throw new EntityNotFoundException("Priority with the id " + priorityId + " is not found");
        }
    }

    @Override
    public PriorityDto save(Priority priority) {
        String priorityName = priority.getName();
        if (!priorityRepository.existsByName(priorityName)) {
            log.info("Saving priority with the name " + priorityName);
            return new PriorityDto(priorityRepository.save(priority));
        } else {
            log.error("Saving priority: priority with the name " + priorityName + " already exists");
            throw new EntityAlreadyExistsException("Priority with the name " + priorityName + " already exists");
        }
    }

    @Override
    public PriorityDto delete(int id) {
        return priorityRepository.findById(id)
                .map(priority -> {
                    priorityRepository.deleteById(id);
                    log.info("Priority with the id " + id + " is deleted");
                    return new PriorityDto(priority);
                })
                .orElseThrow(() -> {
                    log.error("Deleting priority: priority with the id " + id + " is not found");
                    return new EntityNotFoundException("Priority with the id " + id + " is not found");
                });
    }
}
