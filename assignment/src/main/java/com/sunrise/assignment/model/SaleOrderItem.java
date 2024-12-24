package com.sunrise.assignment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sale_order_items")
public class SaleOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double salePrice;

    @Column(nullable = false)
    private Double costAtSale;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleOrder getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(SaleOrder saleOrder) {
        this.saleOrder = saleOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
