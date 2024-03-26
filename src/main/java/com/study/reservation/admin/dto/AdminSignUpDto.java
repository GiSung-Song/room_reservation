package com.study.reservation.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Admin 회원가입 Request Dto")
public class AdminSignUpDto {

    @Schema(description = "대표자성명")
    @NotBlank(message = "대표자성명은 필수 입력 값 입니다.")
    @Size(min = 2, max = 10, message = "2 ~ 10자로 입력해주세요.")
    private String owner;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 15, message = "8 ~ 15자로 입력해주세요.")
    private String password;

    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "전화번호는 필수 입력 값 입니다.")
    @Size(max = 11, min = 9, message = "11자 이하로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String phoneNumber;

    @Schema(description = "사업자등록번호", example = "1234123412")
    @NotBlank(message = "사업자등록번호는 필수 입력 값 입니다.")
    @Size(max = 10, min = 10, message = "10자로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String companyNumber;

    @Schema(description = "개업일자", example = "20240401")
    @NotBlank(message = "개업일자는 필수 입력 값 입니다.")
    @Size(max = 8, min = 8, message = "8자로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String openDate;

    @Schema(description = "계좌번호", example = "12341234123412")
    @Size(max = 14, min = 14, message = "14자로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String accountNumber;
}
