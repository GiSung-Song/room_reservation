package com.study.reservation.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 Request Dto")
public class MemberSignUpDto {

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 필수 입력 값 입니다.")
    @Size(min = 3, max = 10, message = "3 ~ 10자로 입력해주세요.")
    private String nickname;

    @Schema(description = "이름")
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    @Size(min = 2, max = 10, message = "2 ~ 10자로 입력해주세요.")
    private String name;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 15, message = "8 ~ 15자로 입력해주세요.")
    private String password;

    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "전화번호는 필수 입력 값 입니다.")
    @Size(max = 11, min = 9, message = "11자 이하로 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "숫자로만 입력해주세요.")
    private String phoneNumber;

    @Schema(description = "이메일", example = "test@naver.com")
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "이메일 형식으로 작성해주세요.")
    private String email;

}