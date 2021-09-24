package ru.t1.asavin.techSupportAutomation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import ru.t1.asavin.techSupportAutomation.dto.CategoryDto;
import ru.t1.asavin.techSupportAutomation.entity.Category;
import ru.t1.asavin.techSupportAutomation.service.CategoryService;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.findAll();
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @PutMapping("/categories")
    public CategoryDto updateCategory(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @DeleteMapping("/categories/{categoryId}")
    public CategoryDto deleteCategory(@PathVariable int categoryId) {
        return categoryService.delete(categoryId);
    }
}
