package com.study.reservation.product.dto;

import com.study.reservation.product.etc.ProductType;
import com.study.reservation.room.dto.RoomDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "상품 조회 Response Dto")
public class ProductDto {

    @Schema(description = "숙소명")
    private String productName;

    @Schema(description = "숙소 위치")
    private String location;

    @Schema(description = "숙소 타입")
    private ProductType productType;

    @Schema(description = "숙소 연락처")
    private String phoneNumber;

    @Schema(description = "숙소 이메일")
    private String email;

    @Schema(description = "숙소 설명")
    private String description;

    @Schema(description = "숙소 객실목록")
    private List<RoomDto> roomList;

}
