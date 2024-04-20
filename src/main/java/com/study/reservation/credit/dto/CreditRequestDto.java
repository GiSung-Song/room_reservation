package com.study.reservation.credit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "결제 요청 Request Dto")
public class CreditRequestDto {

    @Schema(description = "사용할 포인트")
    private Integer usePoint;
}
