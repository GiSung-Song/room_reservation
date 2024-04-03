package com.study.reservation.product.entity;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.product.etc.BooleanYNConverter;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product")
    private List<Room> rooms;

    public void updateProductName(String productName) {
        this.productName = productName;;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateIsOperate(Boolean isOperate) {
        this.isOperate = isOperate;
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setProduct(this);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setProduct(null);
    }
}
