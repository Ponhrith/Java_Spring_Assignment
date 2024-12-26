package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.PurchaseOrderResponseDTO;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PurchaseOrderControllerTest {

    @InjectMocks
    private PurchaseOrderController purchaseOrderController;

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPurchaseOrders() {
        List<PurchaseOrderResponseDTO> orders = Arrays.asList(new PurchaseOrderResponseDTO(), new PurchaseOrderResponseDTO());
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(orders);

        ResponseEntity<List<PurchaseOrderResponseDTO>> response = purchaseOrderController.getAllPurchaseOrders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(purchaseOrderService, times(1)).getAllPurchaseOrders();
    }

    @Test
    void testGetPurchaseOrderById() {
        PurchaseOrderResponseDTO order = new PurchaseOrderResponseDTO();
        when(purchaseOrderService.getPurchaseOrderById(1L)).thenReturn(order);

        ResponseEntity<PurchaseOrderResponseDTO> response = purchaseOrderController.getPurchaseOrderById(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(purchaseOrderService, times(1)).getPurchaseOrderById(1L);
    }

    @Test
    void testCreatePurchaseOrder() {
        PurchaseOrder order = new PurchaseOrder();
        PurchaseOrderResponseDTO responseDTO = new PurchaseOrderResponseDTO();
        when(purchaseOrderService.createPurchaseOrder(order)).thenReturn(responseDTO);

        ResponseEntity<PurchaseOrderResponseDTO> response = purchaseOrderController.createPurchaseOrder(order);

        assertEquals(200, response.getStatusCodeValue());
        verify(purchaseOrderService, times(1)).createPurchaseOrder(order);
    }
}
