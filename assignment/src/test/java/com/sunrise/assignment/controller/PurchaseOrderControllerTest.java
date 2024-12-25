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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PurchaseOrderControllerTest {

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @InjectMocks
    private PurchaseOrderController purchaseOrderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPurchaseOrders_shouldReturnEmptyList() {
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PurchaseOrderResponseDTO>> response = purchaseOrderController.getAllPurchaseOrders();

        assertThat(response.getBody()).isEmpty();
        verify(purchaseOrderService, times(1)).getAllPurchaseOrders();
    }

    @Test
    void getPurchaseOrderById_shouldReturnPurchaseOrder() {
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(1L);

        when(purchaseOrderService.getPurchaseOrderById(1L)).thenReturn(dto);

        ResponseEntity<PurchaseOrderResponseDTO> response = purchaseOrderController.getPurchaseOrderById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(purchaseOrderService, times(1)).getPurchaseOrderById(1L);
    }

    @Test
    void createPurchaseOrder_shouldReturnCreatedOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(1L);

        when(purchaseOrderService.createPurchaseOrder(any(PurchaseOrder.class))).thenReturn(dto);

        ResponseEntity<PurchaseOrderResponseDTO> response = purchaseOrderController.createPurchaseOrder(purchaseOrder);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(purchaseOrderService, times(1)).createPurchaseOrder(any(PurchaseOrder.class));
    }
}
