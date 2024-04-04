package com.study.reservation.member.dto;

import com.study.reservation.member.etc.Membership;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "개인정보 Response Dto")
public class MemberInfoResDto {

    private String nickname;
    private String name;
    private String phoneNumber;
    private String email;
    private Membership membership;
    private Integer point;
}
