package com.study.reservation.review.entity;

import com.study.reservation.member.entity.Member;
import com.study.reservation.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String comment;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void updateTime() {
        this.time = LocalDateTime.now();
    }
}
