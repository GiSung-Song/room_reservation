package com.study.reservation.credit.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.config.util.CommonUtils;
import com.study.reservation.credit.dto.CreditReadyDto;
import com.study.reservation.credit.dto.CreditRequestDto;
import com.study.reservation.credit.dto.CreditResponseDto;
import com.study.reservation.credit.payment.dto.KakaoApproveResponseDto;
import com.study.reservation.credit.payment.dto.KakaoCancelResponseDto;
import com.study.reservation.credit.payment.dto.KakaoReadyResponseDto;
import com.study.reservation.credit.payment.service.KakaoPayService;
import com.study.reservation.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
@Tag(name = "Credit", description = "결제 API Document")
public class CreditController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final KakaoPayService kakaoPayService;
    private final CreditService creditService;
    private final CommonUtils commonUtils;

    @Operation(summary = "카카오페이 결제 준비", description = "카카오페이 결제를 준비한다.")
    @PostMapping("/orders/{id}/credit")
    public ResponseEntity<ApiResponse<KakaoReadyResponseDto>> readyCredit(@PathVariable("id") Long orderId, CreditRequestDto creditRequestDto) {
        String email = commonUtils.getAuthUsername();

        //카카오페이 결제를 위한 api 호출 및 데이터 세팅
        KakaoReadyResponseDto kakaoReadyResponseDto = creditService.readyToCredit(orderId, email, creditRequestDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "카카오페이 결제가 요청되었습니다.", kakaoReadyResponseDto));
    }

    @Operation(summary = "카카오페이 결제", description = "카카오페이의 결제완료 후 결제 정보를 안내한다.")
    @GetMapping("/kakao/success")
    public ResponseEntity<ApiResponse<CreditResponseDto>> successCredit(@RequestParam("pg_token") String pgToken) {
        KakaoApproveResponseDto kakaoApproveResponseDto = kakaoPayService.approveToKakaoPay(pgToken);
        CreditResponseDto creditResponseDto = creditService.creditSuccess(kakaoApproveResponseDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "카카오페이 결제가 승인되었습니다.", creditResponseDto));
    }

    @Operation(summary = "카카오페이 환불", description = "카카오페이의 결제를 환불 요청한다.")
    @PostMapping("/orders/{id}/refund")
    public ResponseEntity<ApiResponse<KakaoCancelResponseDto>> refundCredit(@PathVariable("id") Long orderId) {
        String email = commonUtils.getAuthUsername();
        KakaoCancelResponseDto kakaoCancelResponseDto = creditService.refundPay(orderId, email);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "카카오페이 결제가 환불 되었습니다.", kakaoCancelResponseDto));
    }

    @Operation(summary = "카카오페이 결제 취소", description = "카카오페이 결제를 취소한다.")
    @GetMapping("/kakao/cancel")
    public ResponseEntity<ApiResponse<String>> cancelCredit() {
        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "카카오페이 결제를 취소하였습니다."));
    }

    @Operation(summary = "카카오페이 결제 실패", description = "카카오페이 결제가 실패했습니다.")
    @GetMapping("/kakao/fail")
    public ResponseEntity<ApiResponse<String>> failCredit() {
        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "카카오페이 결제가 실패했습니다."));
    }
}
