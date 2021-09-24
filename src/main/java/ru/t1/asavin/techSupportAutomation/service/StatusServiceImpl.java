package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.StatusRepository;
import ru.t1.asavin.techSupportAutomation.dto.StatusDto;
import ru.t1.asavin.techSupportAutomation.entity.Status;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public List<StatusDto> findAll() {
        return statusRepository.findAll().stream().map(StatusDto::new).collect(Collectors.toList());
    }

    @Override
    public StatusDto update(Status status) {
        Long statusId = status.getId();
        if (statusRepository.existsById(statusId)) {
            log.info("Updating status with the id " + statusId);
            return new StatusDto(statusRepository.save(status));
        } else {
            log.error("Updating status: status with the id " + statusId + " is not found");
            throw new EntityNotFoundException("Status with the id " + statusId + " is not found");
        }
    }
}
