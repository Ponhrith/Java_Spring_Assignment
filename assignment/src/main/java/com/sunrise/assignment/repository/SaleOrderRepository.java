package com.sunrise.assignment.repository;

import com.sunrise.assignment.model.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
}
