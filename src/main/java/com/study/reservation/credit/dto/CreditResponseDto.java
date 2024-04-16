package com.study.reservation.credit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "결제 확인 Response Dto")
public class CreditResponseDto {

    private Long orderId;
    private Long creditId;
    private Integer creditPrice;
    private Integer savePoint;
    private Integer usePoint;
    private String productName;
    private String roomNum;
    private String startDate;
    private String endDate;
    private String location;
}
