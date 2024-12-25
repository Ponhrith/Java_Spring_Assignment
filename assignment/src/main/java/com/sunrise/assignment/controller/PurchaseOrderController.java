package com.sunrise.assignment.controller;

import com.sunrise.assignment.dto.PurchaseOrderResponseDTO;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "Purchase Order API", description = "Endpoints for managing purchase orders")
@CrossOrigin("http://localhost:8080/")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    @Operation(summary = "Get all purchase orders", description = "Retrieve a list of all purchase orders")
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllPurchaseOrders() {
        return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID", description = "Retrieve a specific purchase order by its ID")
    public ResponseEntity<PurchaseOrderResponseDTO> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new purchase order", description = "Add a new purchase order and update product inventory")
    public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        return ResponseEntity.ok(purchaseOrderService.createPurchaseOrder(purchaseOrder));
    }
}
