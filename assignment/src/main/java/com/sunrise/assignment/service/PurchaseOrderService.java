package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.dto.PurchaseOrderResponseDTO;
import com.sunrise.assignment.dto.PurchaseOrderItemDTO;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.model.PurchaseOrderItem;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.PurchaseOrderRepository;
import com.sunrise.assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    // Retrieve all purchase orders
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a specific purchase order by ID
    public PurchaseOrderResponseDTO getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
        return mapToDTO(purchaseOrder);
    }

    // Create a new purchase order
    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Get the authenticated user
        User currentUser = userService.getCurrentUser();
        purchaseOrder.setCreatedBy(currentUser);

        // Process each purchase order item
        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getId()));

            // Update product inventory
            product.setQty(product.getQty() + item.getQuantity());

            // Recalculate the average cost
            double totalCost = product.getCost() * product.getQty();
            totalCost += item.getPurchasePrice() * item.getQuantity();
            int totalQty = product.getQty();
            product.setCost(totalCost / totalQty);

            // Save updated product
            productRepository.save(product);

            // Link item to purchase order
            item.setPurchaseOrder(purchaseOrder);
        }

        // Save the purchase order
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        // Map to DTO for a cleaner response
        return mapToDTO(savedPurchaseOrder);
    }

    // Helper method to map PurchaseOrder to DTO
    private PurchaseOrderResponseDTO mapToDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(purchaseOrder.getId());
        dto.setCreatedAt(purchaseOrder.getCreatedAt());
        dto.setCreatedBy(purchaseOrder.getCreatedBy().getUsername());

        List<PurchaseOrderItemDTO> itemDTOs = purchaseOrder.getItems().stream()
                .map(item -> {
                    PurchaseOrderItemDTO itemDTO = new PurchaseOrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPurchasePrice(item.getPurchasePrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}
