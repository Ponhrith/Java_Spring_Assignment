package com.sunrise.assignment.repository;

import com.sunrise.assignment.model.SaleOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderItemRepository extends JpaRepository<SaleOrderItem, Long> {
}
