package com.study.reservation.order.dto;

import com.study.reservation.order.entity.Order;
import com.study.reservation.room.etc.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "주문내역 Response Dto")
public class OrderResDto {

    private Long orderId;
    private Long productId;
    private String productName;
    private String location;
    private Long roomId;
    private String roomNum;
    private RoomType roomType;
    private Integer price;
    private String startDate;
    private String endDate;

    public static OrderResDto toDto(Order order) {
        OrderResDto dto = new OrderResDto();

        dto.setOrderId(order.getId());
        dto.setProductId(order.getRoom().getProduct().getId());
        dto.setProductName(order.getRoom().getProduct().getProductName());
        dto.setLocation(order.getRoom().getProduct().getLocation());
        dto.setRoomId(order.getRoom().getId());
        dto.setRoomNum(order.getRoom().getRoomNum());
        dto.setRoomType(order.getRoom().getRoomType());
        dto.setPrice(order.getPrice());
        dto.setStartDate(order.getStartDate());
        dto.setEndDate(order.getEndDate());

        return dto;
    }
}
