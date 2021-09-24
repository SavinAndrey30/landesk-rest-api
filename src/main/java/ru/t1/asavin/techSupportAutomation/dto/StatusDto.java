package ru.t1.asavin.techSupportAutomation.dto;


import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Incident;
import ru.t1.asavin.techSupportAutomation.entity.Status;

import java.time.LocalDateTime;

@Data
public class StatusDto {

    private Long id;
    private String name;
    private LocalDateTime dateStatusAssignedToIncident;
    private Long incidentId;

    public StatusDto(Status status) {
        this.id = status.getId();
        this.name = status.getName();
        this.dateStatusAssignedToIncident = status.getDateStatusAssignedToIncident();
        this.incidentId = getIncidentDto(status);
    }

    private Long getIncidentDto(Status status) {
        Incident incident = status.getIncident();
        if (!(incident == null)) {
            return status.getIncident().getId();
        }
        return null;
    }
}
