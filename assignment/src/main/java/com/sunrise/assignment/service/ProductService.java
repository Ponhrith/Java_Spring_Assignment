package com.sunrise.assignment.service;

import com.sunrise.assignment.exception.ResourceNotFoundException;
import com.sunrise.assignment.exception.DuplicateResourceException;
import com.sunrise.assignment.model.Product;
import com.sunrise.assignment.model.User;
import com.sunrise.assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    // Retrieve active products
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
    }

    // Retrieve product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    // Create a product
    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new DuplicateResourceException("Product with name '" + product.getName() + "' already exists.");
        }

        User currentUser = userService.getCurrentUser();
        product.setCreatedBy(currentUser);
        product.setUpdatedBy(currentUser);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Initialize cost and quantity
        product.setCost(0.0);
        product.setQty(0);

        return productRepository.save(product);
    }

    // Update product details (excluding inventory)
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setCategory(productDetails.getCategory());

        User currentUser = userService.getCurrentUser();
        product.setUpdatedBy(currentUser);
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    // Soft delete a product
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    // Update inventory and cost dynamically
    public void updateInventoryAndCost(Long productId, int quantityChange, double costChange) {
        Product product = getProductById(productId);

        if (quantityChange > 0) { // Purchase Order
            double totalCost = (product.getCost() * product.getQty()) + (costChange * quantityChange);
            int totalQty = product.getQty() + quantityChange;
            product.setCost(totalQty == 0 ? 0.0 : totalCost / totalQty);
            product.setQty(totalQty);
        } else { // Sale Order
            int updatedQty = product.getQty() + quantityChange;
            if (updatedQty < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setQty(updatedQty);
        }

        productRepository.save(product);
    }
}
