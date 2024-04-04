package com.study.reservation.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "객실 수정 Request Dto")
public class RoomUpdateDto {

    @Schema(description = "객실 번호")
    private String RoomNum;

    @Schema(description = "객실 가격")
    private Integer price;

    @Schema(description = "객실 설명")
    private String description;

    @Schema(description = "객실 운영 여부")
    private Boolean isOperate;

    @Schema(description = "객실 수용 인원")
    private Integer headCount;
}
