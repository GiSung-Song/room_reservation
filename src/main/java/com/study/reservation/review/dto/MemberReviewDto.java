package com.study.reservation.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "회원 리뷰 Response Dto")
public class MemberReviewDto {

    @Schema(description = "상품 이름")
    private String productName;

    @Schema(description = "상품 리뷰")
    private String comment;

    @Schema(description = "상품 평점")
    private BigDecimal rating;

    @Schema(description = "리뷰 시간")
    private LocalDateTime time;

}
