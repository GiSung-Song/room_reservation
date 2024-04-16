package com.study.reservation.credit.entity;

import com.study.reservation.credit.etc.CreditStatus;
import com.study.reservation.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Table
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    @Builder.Default
    private Integer usePoint = 0;

    @Column(nullable = false)
    private Integer savePoint;

    private String tid; //결제 고유번호 20자리

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creditDate = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private CreditStatus creditStatus = CreditStatus.CREDIT_READY;

    public void updateCreditStatus(CreditStatus creditStatus) {
        this.creditStatus = creditStatus;
    }

    public void updateCreditDate() {
        this.creditDate = LocalDateTime.now();
    }

    public void updateSavePoint(Integer savePoint) {
        this.savePoint = savePoint;
    }
}
