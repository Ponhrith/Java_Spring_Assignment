package com.sunrise.assignment.controller;

import com.sunrise.assignment.model.Category;
import com.sunrise.assignment.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Endpoints for managing product categories")
@CrossOrigin("http://localhost:8080/")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Add a new category to the system")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        logger.info("Creating category with name '{}'", category.getName());
        Category createdCategory = categoryService.createCategory(category);
        logger.info("Category '{}' created successfully", category.getName());
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update the details of an existing category")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category by marking it inactive")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("Category deleted successfully");
    }
}
