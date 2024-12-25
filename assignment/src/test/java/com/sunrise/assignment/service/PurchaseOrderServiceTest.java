package com.sunrise.assignment.service;

import com.sunrise.assignment.dto.PurchaseOrderResponseDTO;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.model.PurchaseOrderItem;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePurchaseOrder_Success() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        PurchaseOrderItem item = new PurchaseOrderItem();
        Product product = new Product();
        product.setId(1L); // Set valid product ID
        product.setName("Laptop");

        item.setProduct(product); // Assign product to item
        item.setQuantity(10);
        item.setPurchasePrice(500.0);
        purchaseOrder.setItems(Collections.singletonList(item));

        User user = new User();
        user.setUsername("admin");

        when(userService.getCurrentUser()).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product); // Mock the product service
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(purchaseOrder);

        PurchaseOrderResponseDTO result = purchaseOrderService.createPurchaseOrder(purchaseOrder);

        assertNotNull(result);
        verify(productService, times(1)).updateInventoryAndCost(1L, 10, 500.0);
        verify(purchaseOrderRepository, times(1)).save(purchaseOrder);
    }

}
