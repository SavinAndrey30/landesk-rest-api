package ru.t1.asavin.techSupportAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.t1.asavin.techSupportAutomation.dto.IncidentDto;
import ru.t1.asavin.techSupportAutomation.entity.Incident;
import ru.t1.asavin.techSupportAutomation.entity.Status;
import ru.t1.asavin.techSupportAutomation.service.IncidentService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping("/incidents")
    public List<IncidentDto> getIncidents() {
        return incidentService.findAll();
    }

    @GetMapping("/incidents/{incidentId}")
    public IncidentDto getOneIncident(@PathVariable long incidentId) {
        return incidentService.findById(incidentId);
    }

    @PostMapping("/incidents")
    public IncidentDto createIncident(@RequestPart Incident incident, @RequestPart(value = "screenshotFilename", required = false) MultipartFile screenshotFile) throws IOException {
        return incidentService.save(incident, screenshotFile);
    }

    @PutMapping("/incidents")
    public IncidentDto updateIncident(@RequestBody Incident incident) {
        return incidentService.update(incident);
    }

    @PatchMapping("/incidents/{incidentId}/categories/{categoryId}")
    public IncidentDto updateIncidentCategory(@PathVariable Long incidentId, @PathVariable int categoryId) {
        return incidentService.updateIncidentCategory(incidentId, categoryId);
    }

    @PatchMapping("/incidents/{incidentId}/statuses")
    public IncidentDto updateIncidentStatus(@PathVariable Long incidentId, @RequestBody Status status) {
        return incidentService.updateIncidentStatus(incidentId, status);
    }

    @PatchMapping("/incidents/{incidentId}/priorities/{priorityId}")
    public IncidentDto updateIncidentPriority(@PathVariable Long incidentId, @PathVariable int priorityId) {
        return incidentService.updateIncidentPriority(incidentId, priorityId);
    }

    @PatchMapping("/incidents/{incidentId}/analysts/{analystId}")
    public IncidentDto updateIncidentAnalyst(@PathVariable Long incidentId, @PathVariable Long analystId) {
        return incidentService.updateIncidentAnalyst(incidentId, analystId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/incidents/{incidentId}")
    public IncidentDto deleteIncident(@PathVariable Long incidentId) {
        return incidentService.delete(incidentId);
    }
}
