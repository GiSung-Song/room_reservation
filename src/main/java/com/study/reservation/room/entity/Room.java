package com.study.reservation.room.entity;

import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.BooleanYNConverter;
import com.study.reservation.room.etc.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 20)
    private String roomNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer headCount;

    @Column(length = 50000)
    private String description;

    @Column(nullable = false)
    @Convert(converter = BooleanYNConverter.class)
    @Builder.Default
    private Boolean isOperate = true;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void updateHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateIsOperate(Boolean isOperate) {
        this.isOperate = isOperate;
    }
}
