package com.study.reservation.admin.repository;

import com.study.reservation.admin.entity.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    Admin makeAdmin() {
        Admin admin = Admin.builder()
                .password("1234")
                .phoneNumber("01012341234")
                .owner("테스터")
                .accountNumber("12341234123412")
                .openDate("20230103")
                .companyNumber("1234123412")
                .build();

        return admin;
    }

    @Test
    @DisplayName("저장 테스트")
    void 저장_테스트() {
        Admin admin = makeAdmin();

        Admin savedAdmin = adminRepository.save(admin);

        assertEquals(savedAdmin.getAccountNumber(), admin.getAccountNumber());
        assertEquals(savedAdmin.getPhoneNumber(), admin.getPhoneNumber());
    }

    @Test
    @DisplayName("조회 테스트")
    void 조회_테스트() {
        Admin admin = makeAdmin();

        Admin savedAdmin = adminRepository.save(admin);

        Admin findAdmin = adminRepository.findById(savedAdmin.getId()).get();

        assertEquals(findAdmin.getOwner(), admin.getOwner());
        assertEquals(findAdmin.getOpenDate(), admin.getOpenDate());
    }

    @Test
    @DisplayName("사업자번호로 조회 테스트")
    void 사업자등록번호_조회_테스트() {
        Admin admin = makeAdmin();
        adminRepository.save(admin);

        Admin findAdmin = adminRepository.findByCompanyNumber(admin.getCompanyNumber()).get();

        assertEquals(findAdmin.getCompanyNumber(), admin.getCompanyNumber());
        assertEquals(findAdmin.getOpenDate(), admin.getOpenDate());
        assertEquals(findAdmin.getOwner(), admin.getOwner());
    }
}