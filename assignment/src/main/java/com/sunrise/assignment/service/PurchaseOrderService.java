package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.dto.PurchaseOrderResponseDTO;
import com.sunrise.assignment.dto.PurchaseOrderItemDTO;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.model.PurchaseOrderItem;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserService userService;

    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PurchaseOrderResponseDTO getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
        return mapToDTO(purchaseOrder);
    }

    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Fetch the authenticated user
        User currentUser = userService.getCurrentUser();
        purchaseOrder.setCreatedBy(currentUser);

        // Link each item to the purchase order
        purchaseOrder.getItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));

        // Save the purchase order
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        // Convert to DTO and return
        return mapToDTO(savedPurchaseOrder);
    }

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
