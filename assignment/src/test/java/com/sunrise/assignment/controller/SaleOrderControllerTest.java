package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.service.SaleOrderService;
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

class SaleOrderControllerTest {

    @Mock
    private SaleOrderService saleOrderService;

    @InjectMocks
    private SaleOrderController saleOrderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSaleOrders_shouldReturnEmptyList() {
        when(saleOrderService.getAllSaleOrders()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SaleOrderResponseDTO>> response = saleOrderController.getAllSaleOrders();

        assertThat(response.getBody()).isEmpty();
        verify(saleOrderService, times(1)).getAllSaleOrders();
    }

    @Test
    void getSaleOrderById_shouldReturnSaleOrder() {
        SaleOrderResponseDTO dto = new SaleOrderResponseDTO();
        dto.setId(1L);

        when(saleOrderService.getSaleOrderResponseById(1L)).thenReturn(dto);

        ResponseEntity<SaleOrderResponseDTO> response = saleOrderController.getSaleOrderById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(saleOrderService, times(1)).getSaleOrderResponseById(1L);
    }

    @Test
    void createSaleOrder_shouldReturnCreatedOrder() {
        SaleOrder saleOrder = new SaleOrder();
        SaleOrderResponseDTO dto = new SaleOrderResponseDTO();
        dto.setId(1L);

        when(saleOrderService.createSaleOrder(any(SaleOrder.class))).thenReturn(dto);

        ResponseEntity<SaleOrderResponseDTO> response = saleOrderController.createSaleOrder(saleOrder);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(saleOrderService, times(1)).createSaleOrder(any(SaleOrder.class));
    }
}
