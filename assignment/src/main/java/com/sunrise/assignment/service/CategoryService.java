package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.DuplicateCategoryException;
import com.sunrise.assignment.model.Category;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.CategoryRepository;
import com.sunrise.assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private UserRepository userRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category, String username) {
        logger.info("Received request to create category: {}", category.getName());

        // Check if the category already exists
        boolean exists = categoryRepository.existsByName(category.getName());
        logger.info("Category '{}' exists: {}", category.getName(), exists);

        if (exists) {
            logger.warn("Duplicate category detected: {}", category.getName());
            throw new DuplicateCategoryException("Category with name '" + category.getName() + "' already exists.");
        }

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User '{}' not found", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("Creating category '{}' by user '{}'", category.getName(), username);

        // Set metadata
        category.setCreatedBy(user);
        category.setUpdatedBy(user);

        Category savedCategory = categoryRepository.save(category);
        logger.info("Category '{}' successfully created with ID {}", category.getName(), savedCategory.getId());

        return savedCategory;
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        User currentUser = getAuthenticatedUser();
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

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
