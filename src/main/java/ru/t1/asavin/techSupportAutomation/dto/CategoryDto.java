package ru.t1.asavin.techSupportAutomation.dto;

import lombok.Data;
import ru.t1.asavin.techSupportAutomation.entity.Category;

@Data
public class CategoryDto {

    private int id;
    private String name;
    private String description;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
