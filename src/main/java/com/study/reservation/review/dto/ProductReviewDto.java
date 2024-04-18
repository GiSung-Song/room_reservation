package com.study.reservation.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "상품 리뷰 Response Dto")
public class ProductReviewDto {

    @Schema(description = "리뷰 개수")
    private Integer reviewCount;

    @Schema(description = "리뷰 평점")
    private BigDecimal ratingAvg;

    @Schema(description = "리뷰 목록")
    private List<ProductReviewDetailDto> productReviewDetailDtoList;
}
