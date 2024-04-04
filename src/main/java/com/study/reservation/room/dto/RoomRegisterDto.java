package com.study.reservation.room.dto;

import com.study.reservation.config.etc.ValidEnum;
import com.study.reservation.room.etc.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "객실 등록 Request Dto")
public class RoomRegisterDto {

    @Schema(description = "객실 번호")
    @NotBlank(message = "객실 번호는 필수 입력 값 입니다.")
    @Max(20)
    private String RoomNum;

    @Schema(description = "객실 타입", example = "ONE_ROOM, TWO_ROOM, SINGLE_ROOM, TWIN_ROOM ...")
    @NotBlank(message = "객실 타입은 필수 입력 값 입니다.")
    @ValidEnum(message = "ONE_ROOM, TWO_ROOM, SINGLE_ROOM, TWIN_ROOM ...", enumClass = RoomType.class)
    private RoomType roomType;

    @Schema(description = "객실 가격")
    @NotBlank(message = "가격은 필수 입력 값 입니다.")
    private int price;

    @Schema(description = "객실 인원")
    @NotBlank(message = "객실 수용 인원은 필수 입력 값 입니다.")
    private int headCount;

    @Schema(description = "객실 설명")
    private String description;

}
