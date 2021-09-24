package ru.t1.asavin.techSupportAutomation.dto;


import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Incident;
import ru.t1.asavin.techSupportAutomation.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class IncidentDto {

    private Long id;
    private String name;
    private String errorDescription;
    private LocalDateTime dateCreated;
    private LocalDateTime deadline;
    private LocalDateTime dateClosed;
    private DepartmentDto department;
    private String initiatorFullName;
    private String initiatorUsername;
    private String initiatorPhone;
    private String initiatorLocation;
    private String initiatorEmail;
    private List<StatusDto> statusUpdates;
    private UserDto assignedAnalyst;
    private PriorityDto priority;
    private CategoryDto category;

    public IncidentDto(Incident incident) {
        this.id = incident.getId();
        this.name = incident.getName();
        this.errorDescription = incident.getErrorDescription();
        this.dateCreated = incident.getDateCreated();
        this.deadline = incident.getDeadline();
        this.dateClosed = incident.getDateClosed();
        this.department = new DepartmentDto(incident.getDepartment());
        this.initiatorFullName = incident.getInitiatorFullName();
        this.initiatorUsername = incident.getInitiatorUsername();
        this.initiatorPhone = incident.getInitiatorPhone();
        this.initiatorLocation = incident.getInitiatorLocation();
        this.initiatorEmail = incident.getInitiatorEmail();
        this.statusUpdates = getStatusUpdatesDto(incident);
        this.assignedAnalyst = getAnalystDto(incident);
        this.priority = new PriorityDto(incident.getPriority());
        this.category = new CategoryDto(incident.getCategory());
    }

    private UserDto getAnalystDto(Incident incident) {
        User analystAssignedToIncident = incident.getAssignedAnalyst();
        if (!(analystAssignedToIncident == null)) {
            return new UserDto(analystAssignedToIncident);
        }
        return null;
    }

    private List<StatusDto> getStatusUpdatesDto(Incident incident) {
        return incident.getStatusUpdates()
                .stream()
                .map(StatusDto::new)
                .collect(Collectors.toList());
    }
}
