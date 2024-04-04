package com.study.reservation.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "숙소 수정 Request Dto")
public class ProductUpdateDto {

    @Schema(description = "숙소명")
    private String productName;

    @Schema(description = "숙소 위치")
    private String location;

    @Schema(description = "숙소 번호")
    private String phoneNumber;

    @Schema(description = "숙소 설명")
    private String description;

    @Schema(description = "숙소 이메일")
    private String email;

    @Schema(description = "숙소 운영 여부")
    private Boolean operate;

}
