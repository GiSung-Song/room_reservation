package com.study.reservation.admin.controller;

import com.study.reservation.admin.dto.AdminSignUpDto;
import com.study.reservation.admin.service.AdminService;
import com.study.reservation.config.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 API Document")
@RequestMapping("/admin")
public class AdminController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final AdminService adminService;

    @Operation(summary = "회원가입", description = "회원가입을 진행한다.")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> signUp(@Valid @RequestBody AdminSignUpDto adminSignUpDto) {

        //사업자등록번호 검증 api 추가해야함.

        adminService.signUp(adminSignUpDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, HTTP_STATUS_OK.toString(), "회원가입에 성공하였습니다."));
    }

}
