package ru.t1.asavin.techSupportAutomation.service;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.t1.asavin.techSupportAutomation.dao.CategoryRepository;
import ru.t1.asavin.techSupportAutomation.dao.IncidentRepository;
import ru.t1.asavin.techSupportAutomation.dao.PriorityRepository;
import ru.t1.asavin.techSupportAutomation.dao.UserRepository;
import ru.t1.asavin.techSupportAutomation.dto.CategoryDto;
import ru.t1.asavin.techSupportAutomation.dto.IncidentDto;
import ru.t1.asavin.techSupportAutomation.dto.PriorityDto;
import ru.t1.asavin.techSupportAutomation.dto.UserDto;
import ru.t1.asavin.techSupportAutomation.entity.*;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;
import ru.t1.asavin.techSupportAutomation.mqActive.producer.MQProducerService;
import ru.t1.asavin.techSupportAutomation.service.util.ServiceTestsUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@Log
class IncidentServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "Не включается компьютер";
    private static final String SCREENSHOT_FILENAME = null;
    private static final String ERROR_DESCRIPTION = "Компьютер не включается";
    private static final LocalDateTime DATE_CREATED = LocalDateTime.now();
    private static final LocalDateTime DEADLINE = DATE_CREATED.plusDays(3);
    private static final LocalDateTime DATE_CLOSED = DATE_CREATED.plusDays(2);
    private static final Department DEPARTMENT = new Department(1L, "НИЦ", "НИЦ");
    private static final Priority PRIORITY = new Priority(1, "Высокий");
    private static final Category CATEGORY = new Category(1, "Аппаратная проблема", "Проблема с оборудованием");
    private static final User ANALYST = new User(1L, "andrey", "pass123", "andr@landesk.com");
    private static final List<Status> STATUSES = new ArrayList<>(List.of(new Status(Stage.OPEN.getName())));

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PriorityRepository priorityRepository;

    @Mock
    private MQProducerService mqProducerService;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    @Test
    void findAll() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        List<Incident> incidents = List.of(incident);
        List<IncidentDto> actualIncidents = List.of(new IncidentDto(incident));

        doReturn(incidents).when(incidentRepository).findAll();

        List<IncidentDto> expectedIncidentsDto = incidentService.findAll();

        assertThat(expectedIncidentsDto).isEqualTo(actualIncidents);
    }

    @Test
    void update() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);

        doReturn(true).when(incidentRepository).existsById(ID);
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto updatedIncidentDto = incidentService.update(incident);

        assertThat(updatedIncidentDto).isNotNull();
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void update_shouldThrowEntityNotFoundException() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);

        doReturn(false).when(incidentRepository).existsById(ID);

        assertThrows(EntityNotFoundException.class, () -> incidentService.update(incident));
    }

    @Test
    void updateIncidentCategory() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        int categoryId = CATEGORY.getId();

        doReturn(Optional.of(CATEGORY)).when(categoryRepository).findById(categoryId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto updatedIncidentDto = incidentService.updateIncidentCategory(ID, categoryId);

        verify(incidentRepository).save(any(Incident.class));
        assertEquals(new CategoryDto(CATEGORY), updatedIncidentDto.getCategory());
    }

    @Test
    void updateIncidentCategory_EntityNotFoundException() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        int absentCategoryId = 99;

        doReturn(Optional.empty()).when(categoryRepository).findById(absentCategoryId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);

        assertThrows(EntityNotFoundException.class, () -> incidentService.updateIncidentCategory(ID, absentCategoryId));
    }

    @Test
    void updateIncidentPriority() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        int priorityId = PRIORITY.getId();

        doReturn(Optional.of(PRIORITY)).when(priorityRepository).findById(priorityId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto updatedIncidentDto = incidentService.updateIncidentPriority(ID, priorityId);

        verify(incidentRepository).save(any(Incident.class));
        assertEquals(new PriorityDto(PRIORITY), updatedIncidentDto.getPriority());
    }

    @Test
    void updateIncidentPriority_EntityNotFoundException() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        int absentPriorityId = 99;

        doReturn(Optional.empty()).when(priorityRepository).findById(absentPriorityId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);

        assertThrows(EntityNotFoundException.class, () -> incidentService.updateIncidentPriority(ID, absentPriorityId));
    }

    @Test
    void updateIncidentAnalyst() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        long analystId = ANALYST.getId();

        doReturn(Optional.of(ANALYST)).when(userRepository).findById(analystId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto updatedIncidentDto = incidentService.updateIncidentAnalyst(ID, analystId);

        verify(incidentRepository).save(any(Incident.class));
        assertEquals(new UserDto(ANALYST), updatedIncidentDto.getAssignedAnalyst());
    }

    @Test
    void updateIncidentAnalyst_EntityNotFoundException() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        Long absentAnalystId = 99L;

        doReturn(Optional.empty()).when(userRepository).findById(absentAnalystId);
        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);

        assertThrows(EntityNotFoundException.class, () -> incidentService.updateIncidentAnalyst(ID, absentAnalystId));
    }

    @Test
    void updateIncidentStatus() {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        Status status = new Status(Stage.PENDING.getName());

        doReturn(Optional.of(incident)).when(incidentRepository).findById(ID);
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto updatedIncident = incidentService.updateIncidentStatus(ID, status);

        assertEquals(updatedIncident.getStatusUpdates().size(), 2);
    }

    @Test
    void save() throws IOException {
        Incident incident = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        User user = ServiceTestsUtil.createUserObject(1L, "andrey", "pass123", null, null, null, "and@landesk.com", null);

        Authentication auth = ServiceTestsUtil.setupSecurityContext(user);

        doReturn(Optional.of(user)).when(userRepository).findByUsername(auth.getName());
        doReturn(incident).when(incidentRepository).save(incident);

        IncidentDto savedIncidentDto = incidentService.save(incident, null);

        assertNotNull(incidentRepository.findById(savedIncidentDto.getId()));
    }

    @Test
    void delete() {
        Incident incidentToDelete = createIncidentObject(ID, NAME, SCREENSHOT_FILENAME, ERROR_DESCRIPTION, DATE_CREATED, DEADLINE, DATE_CLOSED, DEPARTMENT, PRIORITY, CATEGORY, ANALYST, STATUSES);
        IncidentDto incidentToDeleteDto = new IncidentDto(incidentToDelete);

        doReturn(Optional.of(incidentToDelete)).when(incidentRepository).findById(ID);

        IncidentDto deletedIncidentDto = incidentService.delete(ID);

        assertThat(incidentRepository.existsById(ID)).isFalse();
        assertEquals(incidentToDeleteDto, deletedIncidentDto);
    }

    public Incident createIncidentObject(Long id, String name, String screenshotFilename, String errorDescription, LocalDateTime dateCreated, LocalDateTime deadline, LocalDateTime dateClosed, Department department, Priority priority, Category category, User analyst, List<Status> statuses) {
        Incident incident = new Incident();
        incident.setId(id);
        incident.setName(name);
        incident.setScreenshotFilename(screenshotFilename);
        incident.setErrorDescription(errorDescription);
        incident.setDateCreated(dateCreated);
        incident.setDeadline(deadline);
        incident.setDateClosed(dateClosed);
        incident.setDepartment(department);
        incident.setPriority(priority);
        incident.setCategory(category);
        incident.setAssignedAnalyst(analyst);
        incident.setStatusUpdates(statuses);

        return incident;
    }
}