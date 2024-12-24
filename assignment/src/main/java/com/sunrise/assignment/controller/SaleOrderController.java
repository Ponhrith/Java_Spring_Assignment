package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sale-orders")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping
    public List<SaleOrderResponseDTO> getAllSaleOrders() {
        return saleOrderService.getAllSaleOrders()
                .stream()
                .map(saleOrderService::mapToDTO) // Map each SaleOrder to its DTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderResponseDTO> getSaleOrderById(@PathVariable Long id) {
        SaleOrder saleOrder = saleOrderService.getSaleOrderById(id);
        return ResponseEntity.ok(saleOrderService.mapToDTO(saleOrder)); // Map the SaleOrder to its DTO
    }

    @PostMapping
    public ResponseEntity<SaleOrderResponseDTO> createSaleOrder(@RequestBody SaleOrder saleOrder) {
        SaleOrderResponseDTO response = saleOrderService.createSaleOrder(saleOrder);
        return ResponseEntity.ok(response);
    }
}
