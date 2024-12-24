package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.model.SaleOrderItem;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.SaleOrderItemRepository;
import com.sunrise.assignment.repository.SaleOrderRepository;
import com.sunrise.assignment.repository.ProductRepository;
import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.dto.SaleOrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private SaleOrderItemRepository saleOrderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public List<SaleOrder> getAllSaleOrders() {
        return saleOrderRepository.findAll();
    }

    public SaleOrder getSaleOrderById(Long id) {
        return saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale Order not found with ID: " + id));
    }

    public SaleOrderResponseDTO createSaleOrder(SaleOrder saleOrder) {
        // Get the current authenticated user
        User currentUser = userService.getCurrentUser();
        saleOrder.setCreatedBy(currentUser);

        // Process SaleOrderItems
        for (SaleOrderItem item : saleOrder.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getId()));

            // Check inventory and reduce quantity
            if (product.getQty() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setQty(product.getQty() - item.getQuantity());

            // Set cost at sale
            item.setCostAtSale(product.getCost());
            item.setSaleOrder(saleOrder);

            productRepository.save(product);
        }

        SaleOrder savedSaleOrder = saleOrderRepository.save(saleOrder);

        // Map to DTO for cleaner response
        return mapToDTO(savedSaleOrder);
    }

    public SaleOrderResponseDTO mapToDTO(SaleOrder saleOrder) {
        SaleOrderResponseDTO dto = new SaleOrderResponseDTO();
        dto.setId(saleOrder.getId());
        dto.setCreatedBy(saleOrder.getCreatedBy().getUsername());
        dto.setCreatedAt(saleOrder.getCreatedAt());

        List<SaleOrderItemDTO> items = saleOrder.getItems().stream()
                .map(item -> {
                    SaleOrderItemDTO itemDTO = new SaleOrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setSalePrice(item.getSalePrice());
                    itemDTO.setCostAtSale(item.getCostAtSale());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}
