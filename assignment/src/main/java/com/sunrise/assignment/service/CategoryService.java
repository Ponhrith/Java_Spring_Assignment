package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.DuplicateCategoryException;
import com.sunrise.assignment.model.Category;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        logger.info("Received request to create category: {}", category.getName());

        if (categoryRepository.existsByName(category.getName())) {
            logger.warn("Duplicate category detected: {}", category.getName());
            throw new DuplicateCategoryException("Category with name '" + category.getName() + "' already exists.");
        }

        User currentUser = userService.getCurrentUser();
        category.setCreatedBy(currentUser);
        category.setUpdatedBy(currentUser);

        Category savedCategory = categoryRepository.save(category);
        logger.info("Category '{}' successfully created with ID {}", category.getName(), savedCategory.getId());

        return savedCategory;
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        User currentUser = userService.getCurrentUser();
        category.setName(categoryDetails.getName());
        category.setUpdatedBy(currentUser);
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}
