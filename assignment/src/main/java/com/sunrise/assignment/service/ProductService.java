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

    // Retrieve all products

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isActive) // Only return active products
                .collect(Collectors.toList());
    }




    // Retrieve a specific product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    // Create a new product
    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new DuplicateResourceException("Product with name '" + product.getName() + "' already exists.");
        }

        User currentUser = userService.getCurrentUser();
        product.setCreatedBy(currentUser);
        product.setUpdatedBy(currentUser);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setCategory(productDetails.getCategory());
        product.setPrice(productDetails.getPrice());

        User currentUser = userService.getCurrentUser();
        product.setUpdatedBy(currentUser);
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        Product product = getProductById(id); // Ensure the product exists
        product.setActive(false); // Mark as inactive
        productRepository.save(product); // Save changes
    }



    // Helper method to update inventory and cost
    public void updateInventoryAndCost(Long productId, int quantity, double costPerUnit) {
        Product product = getProductById(productId);

        // Update inventory
        int updatedQty = product.getQty() + quantity;

        // Recalculate cost (weighted average)
        double updatedCost = ((product.getCost() * product.getQty()) + (costPerUnit * quantity)) / updatedQty;

        product.setQty(updatedQty);
        product.setCost(updatedCost);

        // Save updated product
        productRepository.save(product);
    }
}
