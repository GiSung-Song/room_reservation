package com.study.reservation.credit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Credit 정보 Response Dto")
public class CreditReadyDto {

    @Schema(description = "주문 번호")
    private Long orderId;

    @Schema(description = "회원 이름")
    private String memberName;

    @Schema(description = "회원 전화번호")
    private String phoneNumber;

    @Schema(description = "시작 날짜")
    private String startDate;

    @Schema(description = "종료 날짜")
    private String endDate;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "방 번호")
    private String roomNum;

    @Schema(description = "가격")
    private Integer price;

    @Schema(description = "보유 포인트")
    private Integer point;

}