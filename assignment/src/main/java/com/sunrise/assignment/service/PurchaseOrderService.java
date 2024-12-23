package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.model.PurchaseOrderItem;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.repository.PurchaseOrderItemRepository;
import com.sunrise.assignment.repository.PurchaseOrderRepository;
import com.sunrise.assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Save PurchaseOrder and associated items
        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);

        for (PurchaseOrderItem item : purchaseOrder.getItems()) {
            // Update Product Quantity
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getId()));

            product.setQty(product.getQty() + item.getQuantity());
            productRepository.save(product);

            // Save PurchaseOrderItem
            item.setPurchaseOrder(savedOrder);
            purchaseOrderItemRepository.save(item);
        }

        return savedOrder;
    }
}
