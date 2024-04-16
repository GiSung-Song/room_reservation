package com.study.reservation.order.entity;

import com.study.reservation.member.entity.Member;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.EMPTY_ORDER;

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}
