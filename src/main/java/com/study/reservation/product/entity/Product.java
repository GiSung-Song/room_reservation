package com.study.reservation.product.entity;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.product.etc.BooleanYNConverter;
import com.study.reservation.product.etc.ProductType;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(nullable = false, length = 30)
    private String productName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(length = 50000)
    private String description;

    @Column(nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    @Builder.Default
    private boolean isOperate = true;
}
