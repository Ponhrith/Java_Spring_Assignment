package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.DuplicateCategoryException;
import com.sunrise.assignment.model.Category;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory_Success() {
        Category category = new Category();
        category.setName("Electronics");

        User user = new User();
        user.setUsername("admin");

        when(userService.getCurrentUser()).thenReturn(user);
        when(categoryRepository.existsByName("Electronics")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testCreateCategory_DuplicateCategoryException() {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        assertThrows(DuplicateCategoryException.class, () -> categoryService.createCategory(category));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testGetCategoryById_Success() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isEmpty());
    }
}
