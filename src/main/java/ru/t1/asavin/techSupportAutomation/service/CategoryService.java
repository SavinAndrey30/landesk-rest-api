package ru.t1.asavin.techSupportAutomation.service;

import ru.t1.asavin.techSupportAutomation.dto.CategoryDto;
import ru.t1.asavin.techSupportAutomation.entity.Category;

import java.util.List;


public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto update(Category category);

    CategoryDto save(Category category);

    CategoryDto delete(int id);
}
