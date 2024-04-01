package com.study.reservation.product.repository;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

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
        adminRepository.save(admin);

        Product product = Product.builder()
                .email("test@test.com")
                .description("테스트 설명입니다.")
                .phoneNumber("01012341234")
                .productName("테스트 숙소")
                .productType(ProductType.RESORT)
                .location("테스트시 테스트구 테스트동 41-23")
                .admin(admin)
                .isOperate(true)
                .build();

        Product savedProduct = productRepository.save(product);

        assertEquals(savedProduct.getProductName(), product.getProductName());
        assertEquals(savedProduct.getProductType(), product.getProductType());
    }

    @Test
    @DisplayName("조회 테스트")
    void 조회_테스트() {
        Admin admin = makeAdmin();
        adminRepository.save(admin);


        Product product = Product.builder()
                .email("test@test.com")
                .description("테스트 설명입니다.")
                .phoneNumber("01012341234")
                .productName("테스트 숙소")
                .productType(ProductType.RESORT)
                .location("테스트시 테스트구 테스트동 41-23")
                .admin(admin)
                .isOperate(true)
                .build();

        Product savedProduct = productRepository.save(product);

        Product findProduct = productRepository.findById(savedProduct.getId()).get();

        assertEquals(findProduct.getProductName(), product.getProductName());
        assertEquals(findProduct.getProductType(), product.getProductType());
    }

}