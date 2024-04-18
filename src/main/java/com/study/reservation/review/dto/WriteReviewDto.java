package com.study.reservation.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "리뷰 등록 Request Dto")
public class WriteReviewDto {

    @Schema(description = "리뷰")
    private String comment;

    @Schema(description = "평점")
    private BigDecimal rating;
}
