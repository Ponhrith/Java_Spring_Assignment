package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.DuplicateResourceException;
import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        Product product = new Product();
        product.setName("Laptop");

        User user = new User();
        user.setUsername("admin");

        when(userService.getCurrentUser()).thenReturn(user);
        when(productRepository.existsByName("Laptop")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testCreateProduct_DuplicateResourceException() {
        Product product = new Product();
        product.setName("Laptop");

        when(productRepository.existsByName("Laptop")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetProductById_Success() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductById_ResourceNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }
}
