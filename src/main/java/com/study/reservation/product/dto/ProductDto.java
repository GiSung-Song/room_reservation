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

    private String productName;
    private String location;
    private ProductType productType;
    private String phoneNumber;
    private String email;
    private String description;
    private List<RoomDto> roomList;

}
