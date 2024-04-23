package com.study.reservation.credit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "결제 성공 시 Request Dto")
public class CreditSuccessDto {

    @Schema(description = "사용 포인트")
    private Integer usedPoint;

    @Schema(description = "총 결제 금액")
    private Integer totalPrice;

    @Schema(description = "결제 고유번호")
    private String impUid;

    @Schema(description = "회원 번호")
    private String phoneNumber;

    @Schema(description = "회원 이름")
    private String memberName;

    @Schema(description = "주문 번호")
    private Long orderId;
}
