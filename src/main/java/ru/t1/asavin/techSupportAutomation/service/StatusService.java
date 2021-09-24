package ru.t1.asavin.techSupportAutomation.service;

import ru.t1.asavin.techSupportAutomation.dto.StatusDto;
import ru.t1.asavin.techSupportAutomation.entity.Status;

import java.util.List;


public interface StatusService {

    List<StatusDto> findAll();

    StatusDto update(Status status);
}
