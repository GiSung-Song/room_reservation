package com.study.reservation.credit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditReadyDto {

    private Long orderId; //partner_order_id
    private Long memberId; //partner_user_id
    private Long roomId; //item_code
    private String productName; //item_name
    private Integer totalPrice; //total_amount

}