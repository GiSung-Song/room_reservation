package com.study.reservation.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "RefreshToken을 찾을 수 없습니다."),
    NOT_FOUND_COMPANY_NUMBER(HttpStatus.BAD_REQUEST, "사업자등록번호를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품을 찾을 수 없습니다."),
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "해당 객실을 찾을 수 없습니다."),

    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),

    DUPLICATE_COMPANY_NUMBER(HttpStatus.CONFLICT, "이미 등록된 사업자등록번호입니다."),

    NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 입력입니다."),
    NOT_VALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임은 3 ~ 10자 입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8 ~ 15자 입니다."),
    NOT_VALID_COMPANY(HttpStatus.BAD_REQUEST, "사업자 정보가 올바르지 않습니다."),
    NOT_VALID_ADMIN(HttpStatus.BAD_REQUEST, "해당 사업자의 관리자가 아닙니다."),
    NOT_VALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "핸드폰 번호는 9 ~ 11자 입니다."),
    NOT_VALID_NUMBER(HttpStatus.BAD_REQUEST, "숫자로만 입력해주세요."),
    NOT_VALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식으로 입력해주세요."),
    NOT_VALID_ROOM_NUM(HttpStatus.BAD_REQUEST, "숙소명은 최대 30자 입니다."),

    NOT_VALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "숙소명은 30자 이하입니다."),

    CLOSE_COMPANY_NUMBER(HttpStatus.BAD_REQUEST, "휴업중이거나 폐업한 사업자번호입니다."),

    OPEN_API_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OPEN API 요청 중 오류가 발생하였습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}