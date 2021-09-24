package ru.t1.asavin.techSupportAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.asavin.techSupportAutomation.dto.StatusDto;
import ru.t1.asavin.techSupportAutomation.entity.Status;
import ru.t1.asavin.techSupportAutomation.service.StatusService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping("/statuses")
    public List<StatusDto> getStatuses() {
        return statusService.findAll();
    }

    @PutMapping("/statuses")
    public StatusDto updateStatus(@RequestBody Status status) {
        return statusService.update(status);
    }
}
