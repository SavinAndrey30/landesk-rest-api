package ru.t1.asavin.techSupportAutomation.dto;

import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Priority;

@Data
public class PriorityDto {

    private int id;
    private String name;

    public PriorityDto(Priority priority) {
        this.id = priority.getId();
        this.name = priority.getName();
    }
}
