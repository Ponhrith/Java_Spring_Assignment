package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.service.SaleOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-orders")
@Tag(name = "Sale Order API", description = "Endpoints for managing sale orders")
@CrossOrigin("http://localhost:8080/")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping
    @Operation(summary = "Get all sale orders", description = "Retrieve a list of all sale orders")
    public ResponseEntity<List<SaleOrderResponseDTO>> getAllSaleOrders() {
        return ResponseEntity.ok(saleOrderService.getAllSaleOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale order by ID", description = "Retrieve a specific sale order by its ID")
    public ResponseEntity<SaleOrderResponseDTO> getSaleOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(saleOrderService.getSaleOrderResponseById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new sale order", description = "Add a new sale order and update product inventory")
    public ResponseEntity<SaleOrderResponseDTO> createSaleOrder(@RequestBody SaleOrder saleOrder) {
        return ResponseEntity.ok(saleOrderService.createSaleOrder(saleOrder));
    }
}
