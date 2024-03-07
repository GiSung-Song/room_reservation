package com.study.reservation.member.controller;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) {
        log.info("post : signUp");

        memberService.signUp(memberSignUpDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, HTTP_STATUS_OK.toString(), "회원가입에 성공하였습니다."));
    }

}
