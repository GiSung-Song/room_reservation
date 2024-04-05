package com.study.reservation.room.dto;

import com.study.reservation.room.etc.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "객실 조회 Response Dto")
public class RoomDto {

    private String productName;
    private String roomNum;
    private RoomType roomType;
    private Integer price;
    private Integer headCount;
    private String description;
    private Boolean isOperate;

}
