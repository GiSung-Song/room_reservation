package com.study.reservation.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),

    DUPLICATE_COMPANY_NUMBER(HttpStatus.CONFLICT, "이미 등록된 사업자등록번호입니다."),

    NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 입력입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}