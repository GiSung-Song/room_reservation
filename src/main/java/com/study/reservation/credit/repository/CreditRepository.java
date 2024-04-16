package com.study.reservation.credit.repository;

import com.study.reservation.credit.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByOrder_Id(Long orderId);
}
