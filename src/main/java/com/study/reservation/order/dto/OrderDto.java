package com.study.reservation.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "주문정보 Request Dto")
public class OrderDto {

    @Schema(description = "인원", example = "2")
    @NotBlank(message = "인원은 필수 입력 값 입니다.")
    private Integer headCount;

    @Schema(description = "시작날짜", example = "20240410")
    @NotBlank(message = "시작날짜는 필수 입력 값 입니다.")
    @Length(min = 8, max = 8, message = "8자로 입력해주세요.")
    private String startDate;

    @Schema(description = "종료날짜", example = "20240410")
    @NotBlank(message = "종료날짜는 필수 입력 값 입니다.")
    @Length(min = 8, max = 8, message = "8자로 입력해주세요.")
    private String endDate;
}
