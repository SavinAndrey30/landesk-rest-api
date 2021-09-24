package ru.t1.asavin.techSupportAutomation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.asavin.techSupportAutomation.dao.CategoryRepository;
import ru.t1.asavin.techSupportAutomation.dto.CategoryDto;
import ru.t1.asavin.techSupportAutomation.entity.Category;
import ru.t1.asavin.techSupportAutomation.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private static final int ID = 1;
    private static final String NAME = "ПО";
    private static final String DESCRIPTION = "программное обеспечение";

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll() {
        Category category = createCategoryObject(ID, NAME, DESCRIPTION);
        List<Category> categories = List.of(category);
        List<CategoryDto> actualCategories = List.of(new CategoryDto(category));

        doReturn(categories).when(categoryRepository).findAll();

        List<CategoryDto> expectedCategories = categoryService.findAll();

        assertThat(expectedCategories).isEqualTo(actualCategories);
    }

    @Test
    void update() {
        Category category = createCategoryObject(ID, NAME, DESCRIPTION);

        doReturn(true).when(categoryRepository).existsById(ID);
        doReturn(category).when(categoryRepository).save(category);

        CategoryDto updatedCategoryDto = categoryService.update(category);
        assertThat(updatedCategoryDto).isNotNull();

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void save() {
        Category category = createCategoryObject(ID, NAME, DESCRIPTION);
        CategoryDto categoryDto = new CategoryDto(category);

        doReturn(category).when(categoryRepository).save(category);

        CategoryDto savedCategoryDto = categoryService.save(category);

        assertEquals(savedCategoryDto, categoryDto);
        assertNotNull(categoryRepository.findById(savedCategoryDto.getId()));
    }

    @Test
    void save_shouldThrowEntityAlreadyExistsException() {
        Category category = createCategoryObject(ID, NAME, DESCRIPTION);

        doReturn(true).when(categoryRepository).existsByName(NAME);

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.save(category));
    }

    @Test
    void delete() {
        Category categoryToDelete = createCategoryObject(ID, NAME, DESCRIPTION);
        CategoryDto categoryToDeleteDto = new CategoryDto(categoryToDelete);

        doReturn(Optional.of(categoryToDelete)).when(categoryRepository).findById(ID);

        CategoryDto deletedCategoryDto = categoryService.delete(ID);
        assertThat(categoryRepository.existsById(ID)).isFalse();
        assertEquals(categoryToDeleteDto, deletedCategoryDto);
    }

    public Category createCategoryObject(int id, String name, String description) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);

        return category;
    }
}