package com.study.reservation.credit.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.config.util.CommonUtils;
import com.study.reservation.credit.dto.CreditReadyDto;
import com.study.reservation.credit.dto.CreditSuccessDto;
import com.study.reservation.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Credit", description = "결제 API Document")
@Controller
public class CreditController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final CreditService creditService;
    private final CommonUtils commonUtils;

    @GetMapping("/credit/{id}")
    public String readyCredit(@PathVariable("id") Long orderId, Model model) {
        String email = commonUtils.getAuthUsername();

        //카카오페이 결제를 위한 api 호출 및 데이터 세팅
        CreditReadyDto readyDto = creditService.readyToCredit(orderId, email);
        model.addAttribute("readyDto", readyDto);

        return "credit/credit";
    }

    @Operation(summary = "결제 성공", description = "결제가 성공한다.")
    @ResponseBody
    @PostMapping("/credit/success")
    public ResponseEntity<ApiResponse<String>> successCredit(@RequestBody CreditSuccessDto creditSuccessDto) {
        String email = commonUtils.getAuthUsername();

        log.info("creditSuccessDto : {}", creditSuccessDto);

        creditService.successCredit(creditSuccessDto, email);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "결제 및 예약이 완료되었습니다."));
    }

}
