package com.study.reservation.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원정보 수정 Request Dto")
public class MemberUpdateReqDto {

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "비밀번호")
    private String password;

}
