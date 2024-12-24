package com.sunrise.assignment.service;

import com.sunrise.assignment.dto.SaleOrderResponseDTO;
import com.sunrise.assignment.dto.SaleOrderItemDTO;
import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.SaleOrder;
import com.sunrise.assignment.model.SaleOrderItem;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.SaleOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    public List<SaleOrderResponseDTO> getAllSaleOrders() {
        return saleOrderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SaleOrderResponseDTO getSaleOrderResponseById(Long id) {
        SaleOrder saleOrder = saleOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale Order not found with ID: " + id));
        return mapToDTO(saleOrder);
    }

    @Transactional
    public SaleOrderResponseDTO createSaleOrder(SaleOrder saleOrder) {
        User currentUser = userService.getCurrentUser();
        saleOrder.setCreatedBy(currentUser);

        for (SaleOrderItem item : saleOrder.getItems()) {
            Product product = productService.getProductById(item.getProduct().getId());

            if (product.getQty() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Reduce product quantity
            productService.updateInventoryAndCost(
                    product.getId(),
                    -item.getQuantity(),
                    0 // No cost update during sale
            );

            // Update product price to the latest sale price
            product.setPrice(item.getSalePrice());

            // Save updated product
            productService.updateProduct(product.getId(), product);

            item.setCostAtSale(product.getCost());
            item.setSaleOrder(saleOrder);
        }

        SaleOrder savedSaleOrder = saleOrderRepository.save(saleOrder);
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
