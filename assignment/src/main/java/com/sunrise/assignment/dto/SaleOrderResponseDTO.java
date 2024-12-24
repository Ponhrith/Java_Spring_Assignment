package com.sunrise.assignment.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleOrderResponseDTO {
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<SaleOrderItemDTO> items;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SaleOrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleOrderItemDTO> items) {
        this.items = items;
    }
}
