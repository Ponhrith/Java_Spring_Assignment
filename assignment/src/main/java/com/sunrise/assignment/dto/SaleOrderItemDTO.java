package com.sunrise.assignment.dto;

public class SaleOrderItemDTO {
    private Long productId;
    private Integer quantity;
    private Double salePrice;
    private Double costAtSale;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getCostAtSale() {
        return costAtSale;
    }

    public void setCostAtSale(Double costAtSale) {
        this.costAtSale = costAtSale;
    }
}
