package ru.t1.asavin.techSupportAutomation.service;

import org.springframework.web.multipart.MultipartFile;
import ru.t1.asavin.techSupportAutomation.dto.IncidentDto;
import ru.t1.asavin.techSupportAutomation.entity.Incident;
import ru.t1.asavin.techSupportAutomation.entity.Status;

import java.io.IOException;
import java.util.List;


public interface IncidentService {

    List<IncidentDto> findAll();

    IncidentDto findById(Long incidentId);

    IncidentDto update(Incident incident);

    IncidentDto updateIncidentCategory(Long incidentId, int categoryId);

    IncidentDto updateIncidentStatus(Long incidentId, Status status);

    IncidentDto updateIncidentPriority(Long incidentId, int priorityId);

    IncidentDto updateIncidentAnalyst(Long incidentId, Long analystId);

    IncidentDto save(Incident incident, MultipartFile screenshotFile) throws IOException;

    IncidentDto delete(Long id);
}
