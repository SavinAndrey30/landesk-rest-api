package ru.t1.asavin.techSupportAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.asavin.techSupportAutomation.dto.PriorityDto;
import ru.t1.asavin.techSupportAutomation.entity.Priority;
import ru.t1.asavin.techSupportAutomation.service.PriorityService;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping("/priorities")
    public List<PriorityDto> getPriorities() {
        return priorityService.findAll();
    }

    @PostMapping("/priorities")
    public PriorityDto createPriority(@RequestBody Priority priority) {
        return priorityService.save(priority);
    }

    @PutMapping("/priorities")
    public PriorityDto updatePriority(@RequestBody Priority priority) {
        return priorityService.update(priority);
    }

    @DeleteMapping("/priorities/{priorityId}")
    public PriorityDto deletePriority(@PathVariable int priorityId) {
        return priorityService.delete(priorityId);
    }
}
