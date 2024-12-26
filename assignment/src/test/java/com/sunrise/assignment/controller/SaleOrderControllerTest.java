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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SaleOrderControllerTest {

    @InjectMocks
    private SaleOrderController saleOrderController;

    @Mock
    private SaleOrderService saleOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSaleOrders() {
        List<SaleOrderResponseDTO> orders = Arrays.asList(new SaleOrderResponseDTO(), new SaleOrderResponseDTO());
        when(saleOrderService.getAllSaleOrders()).thenReturn(orders);

        ResponseEntity<List<SaleOrderResponseDTO>> response = saleOrderController.getAllSaleOrders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(saleOrderService, times(1)).getAllSaleOrders();
    }

    @Test
    void testGetSaleOrderById() {
        SaleOrderResponseDTO order = new SaleOrderResponseDTO();
        when(saleOrderService.getSaleOrderResponseById(1L)).thenReturn(order);

        ResponseEntity<SaleOrderResponseDTO> response = saleOrderController.getSaleOrderById(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(saleOrderService, times(1)).getSaleOrderResponseById(1L);
    }

    @Test
    void testCreateSaleOrder() {
        SaleOrder order = new SaleOrder();
        SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
        when(saleOrderService.createSaleOrder(order)).thenReturn(responseDTO);

        ResponseEntity<SaleOrderResponseDTO> response = saleOrderController.createSaleOrder(order);

        assertEquals(200, response.getStatusCodeValue());
        verify(saleOrderService, times(1)).createSaleOrder(order);
    }
}
