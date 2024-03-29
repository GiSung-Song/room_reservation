package com.study.reservation.config.jwt.repository;

import com.study.reservation.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByCompanyNumber(String companyNumber);
}
