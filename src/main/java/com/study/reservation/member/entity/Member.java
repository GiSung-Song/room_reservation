package com.study.reservation.member.entity;

import com.study.reservation.config.etc.Role;
import com.study.reservation.member.etc.Membership;
import com.study.reservation.order.entity.Order;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 10)
    private String nickname;

    @Column(unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Membership membership = Membership.BRONZE;

    @Builder.Default
    @Column(nullable = false)
    private Integer point = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer accPoint = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setMember(this);
    }

    public void savePoint(Integer point) {
        this.point += point;
        this.accPoint += point;
    }

    public void refundPoint(Integer point) {
        this.point += point;
    }

    public void cancelSavePoint(Integer point) {
        this.point -= point;
        this.accPoint -= point;
    }

    public void usePoint(Integer point) {
        this.point -= point;
    }

    public void updateMembership() {
        if (accPoint < 10000) {
            setMembership(Membership.BRONZE);
        } else if (accPoint < 20000) {
            setMembership(Membership.SILVER);
        } else if (accPoint < 30000) {
            setMembership(Membership.GOLD);
        } else if (accPoint < 50000) {
            setMembership(Membership.PLATINUM);
        }
    }

    private void setMembership(Membership membership) {
        this.membership = membership;
    }
}
