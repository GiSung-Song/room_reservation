package com.study.reservation.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "상품 조회 Response Dto")

public class ProductListDto {

    @Schema(description = "숙소 ID")
    private Long productId;

    @Schema(description = "숙소명")
    private String productName;

    @Schema(description = "가격")
    private int lowPrice;

    @Schema(description = "숙소 위치")
    private String location;
}
