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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductService productService;

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

    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrder purchaseOrder) {
        User currentUser = userService.getCurrentUser();
        purchaseOrder.setCreatedBy(currentUser);

        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            productService.updateInventoryAndCost(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getPurchasePrice()
            );
            item.setPurchaseOrder(purchaseOrder);
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        return mapToDTO(savedOrder);
    }

    private PurchaseOrderResponseDTO mapToDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(purchaseOrder.getId());
        dto.setCreatedBy(purchaseOrder.getCreatedBy().getUsername());
        dto.setCreatedAt(purchaseOrder.getCreatedAt());

        List<PurchaseOrderItemDTO> items = purchaseOrder.getItems().stream()
                .map(item -> {
                    PurchaseOrderItemDTO itemDTO = new PurchaseOrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPurchasePrice(item.getPurchasePrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}
