package com.study.reservation.product.repository;

import com.study.reservation.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByAdmin_Id(Long admin);
}
