package ru.t1.asavin.techSupportAutomation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.asavin.techSupportAutomation.dao.CategoryRepository;
import ru.t1.asavin.techSupportAutomation.dto.CategoryDto;
import ru.t1.asavin.techSupportAutomation.entity.Category;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;
import ru.t1.asavin.techSupportAutomation.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::new).collect(Collectors.toList());
    }

    @Override
    public CategoryDto update(Category category) {
        int categoryId = category.getId();
        if (categoryRepository.existsById(categoryId)) {
            log.info("Updating category with the id " + categoryId);
            return new CategoryDto(categoryRepository.save(category));
        } else {
            log.error("Updating category: Category with the id " + categoryId + " is not found");
            throw new EntityNotFoundException("Category with the id " + categoryId + " is not found");
        }
    }

    @Override
    public CategoryDto save(Category category) {
        String categoryName = category.getName();
        if (!categoryRepository.existsByName(categoryName)) {
            log.info("Saving category with the name " + categoryName);
            return new CategoryDto(categoryRepository.save(category));
        } else {
            log.error("Saving category: Category " + categoryName + " already exists");
            throw new EntityAlreadyExistsException("Category " + categoryName + " already exists");
        }
    }

    @Override
    public CategoryDto delete(int id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    log.info("Category with the id " + id + " is deleted");
                    return new CategoryDto(category);
                })
                .orElseThrow(() -> {
                    log.error("Deleting category: Category with the id " + id + " is not found");
                    return new EntityNotFoundException("Category with the id " + id + " is not found");
                });
    }
}
