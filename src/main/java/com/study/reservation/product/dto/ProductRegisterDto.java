package com.study.reservation.product.dto;

import com.study.reservation.config.etc.ValidEnum;
import com.study.reservation.product.etc.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Schema(description = "상품 등록 Request Dto")
public class ProductRegisterDto {

    @Schema(description = "숙소명")
    @NotBlank(message = "숙소명은 필수 입력 값 입니다.")
    @Max(30)
    private String productName;

    @Schema(description = "위치", example = "서울시 도봉구 방학로 ...")
    @NotBlank(message = "위치는 필수 입력 값 입니다.")
    private String location;

    @Schema(description = "숙소 타입", example = "HOTEL, HOUSE, MOTEL, RESORT, GUEST_HOUSE, PENSION, PARTY_ROOM, POOL_VILLA")
    @NotBlank(message = "숙소 타입은 필수 입력 값 입니다.")
    @ValidEnum(message = "HOTEL, HOUSE, MOTEL, RESORT ...", enumClass = ProductType.class)
    private ProductType productType;

    @Schema(description = "숙소관련 연락처")
    @NotBlank(message = "숙소관련 연락처는 필수 입력 값 입니다.")
    @Size(max = 11, min = 9, message = "11자 이하로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String phoneNumber;

    @Schema(description = "이메일", example = "test@naver.com")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "이메일 형식으로 작성해주세요.")
    private String email;

    @Schema(description = "숙소 설명")
    private String description;
}
