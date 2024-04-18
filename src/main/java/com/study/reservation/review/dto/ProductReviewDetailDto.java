package com.study.reservation.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "상품 리뷰 상세 Response Dto")
public class ProductReviewDetailDto {

    @Schema(description = "리뷰 ID")
    private Long reviewId;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "별점")
    private BigDecimal rating;

    @Schema(description = "리뷰")
    private String comment;

    @Schema(description = "시간")
    private LocalDateTime time;
}
