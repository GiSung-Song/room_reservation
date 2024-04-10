package com.study.reservation.order.dto;

import com.study.reservation.room.etc.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "주문내역 Response Dto")
public class OrderResDto {

    private String productId;
    private String productName;
    private String location;
    private String roomId;
    private String roomNum;
    private RoomType roomType;
    private String price;
    private String startDate;
    private String endDate;
}
