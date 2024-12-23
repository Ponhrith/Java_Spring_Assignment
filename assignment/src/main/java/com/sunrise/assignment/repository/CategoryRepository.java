package com.sunrise.assignment.repository;

import com.sunrise.assignment.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
