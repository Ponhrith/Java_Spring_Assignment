package com.sunrise.assignment.controller;

import com.sunrise.assignment.model.Category;
import com.sunrise.assignment.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_shouldReturnEmptyList() {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        List<Category> response = categoryController.getAllCategories();

        assertThat(response).isEmpty();
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Electronics");
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Electronics");
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Updated Category");

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, category);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Category");
        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void deleteCategory_shouldDeleteSuccessfully() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<?> response = categoryController.deleteCategory(1L);

        assertThat(response.getBody()).isEqualTo("Category deleted successfully");
        verify(categoryService, times(1)).deleteCategory(1L);
    }
}
