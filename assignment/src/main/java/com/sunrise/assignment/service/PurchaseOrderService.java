package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.model.PurchaseOrder;
import com.sunrise.assignment.model.PurchaseOrderItem;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.PurchaseOrderItemRepository;
import com.sunrise.assignment.repository.PurchaseOrderRepository;
import com.sunrise.assignment.repository.ProductRepository;
import com.sunrise.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Use unified getCurrentUser method
        User currentUser = userService.getCurrentUser();
        purchaseOrder.setCreatedBy(currentUser);

        // Link items to the purchase order
        purchaseOrder.getItems().forEach(item -> {
            item.setPurchaseOrder(purchaseOrder);
        });

        return purchaseOrderRepository.save(purchaseOrder);
    }


}
