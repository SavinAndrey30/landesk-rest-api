package ru.t1.asavin.techSupportAutomation.service;

import ru.t1.asavin.techSupportAutomation.dto.PriorityDto;
import ru.t1.asavin.techSupportAutomation.entity.Priority;

import java.util.List;


public interface PriorityService {

    List<PriorityDto> findAll();

    PriorityDto update(Priority priority);

    PriorityDto save(Priority priority);

    PriorityDto delete(int id);
}
