package com.sunrise.assignment.controller;

import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_shouldReturnEmptyList() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getBody()).isEmpty();
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_shouldReturnProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("MacBook");

        when(productService.getProductById(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("MacBook");
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("MacBook");

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("MacBook");
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(1L, product);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Product");
        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_shouldDeleteSuccessfully() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(productService, times(1)).deleteProduct(1L);
    }
}
