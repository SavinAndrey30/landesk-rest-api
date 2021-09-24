package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.asavin.techSupportAutomation.dao.StatusRepository;
import ru.t1.asavin.techSupportAutomation.dto.StatusDto;
import ru.t1.asavin.techSupportAutomation.entity.Incident;
import ru.t1.asavin.techSupportAutomation.entity.Stage;
import ru.t1.asavin.techSupportAutomation.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class StatusServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = Stage.PENDING.getName();
    private static final LocalDateTime DATE_STATUS_ASSIGNED_TO_INCIDENT = LocalDateTime.now();

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    @Test
    void findAll() {
        Status status = createStatusObject(ID, NAME, DATE_STATUS_ASSIGNED_TO_INCIDENT);
        List<Status> statuses = List.of(status);
        List<StatusDto> actualStatusesDto = List.of(new StatusDto(status));

        doReturn(statuses).when(statusRepository).findAll();

        List<StatusDto> expectedStatusesDto = statusService.findAll();

        assertThat(expectedStatusesDto).isEqualTo(actualStatusesDto);
    }

    @Test
    void update() {
        Status status = createStatusObject(ID, NAME, DATE_STATUS_ASSIGNED_TO_INCIDENT);

        doReturn(true).when(statusRepository).existsById(ID);
        doReturn(status).when(statusRepository).save(status);

        StatusDto updatedStatusDto = statusService.update(status);
        assertThat(updatedStatusDto).isNotNull();

        verify(statusRepository).save(any(Status.class));
    }

    public Status createStatusObject(Long id, String name, LocalDateTime date) {
        Status status = new Status();
        status.setId(id);
        status.setName(name);
        status.setDateStatusAssignedToIncident(date);
        status.setIncident(new Incident());

        return status;
    }
}