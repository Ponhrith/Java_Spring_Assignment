package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-orders")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping
    public ResponseEntity<List<SaleOrderResponseDTO>> getAllSaleOrders() {
        // The service already returns a list of SaleOrderResponseDTO, no need to map again
        List<SaleOrderResponseDTO> saleOrders = saleOrderService.getAllSaleOrders();
        return ResponseEntity.ok(saleOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderResponseDTO> getSaleOrderById(@PathVariable Long id) {
        // Directly map the SaleOrder to its DTO
        SaleOrder saleOrder = saleOrderService.getSaleOrderById(id);
        SaleOrderResponseDTO response = saleOrderService.mapToDTO(saleOrder);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SaleOrderResponseDTO> createSaleOrder(@RequestBody SaleOrder saleOrder) {
        SaleOrderResponseDTO response = saleOrderService.createSaleOrder(saleOrder);
        return ResponseEntity.ok(response);
    }
}
