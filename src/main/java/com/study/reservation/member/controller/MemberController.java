package com.study.reservation.member.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.member.dto.MemberInfoResDto;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API Document")
public class MemberController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입을 진행한다.")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) {
        log.info("post : signUp");

        memberService.signUp(memberSignUpDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, HTTP_STATUS_OK.toString(), "회원가입에 성공하였습니다."));
    }

    @Operation(summary = "개인정보", description = "개인정보를 조회한다.")
    @GetMapping("/member")
    public ResponseEntity<ApiResponse<MemberInfoResDto>> getInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();

        MemberInfoResDto memberInfo = memberService.getMemberInfo(email);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, HTTP_STATUS_OK.toString(), memberInfo));
    }
}
