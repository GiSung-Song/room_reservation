package com.study.reservation.credit.payment.service;

import com.study.reservation.credit.dto.CreditCancelDto;
import com.study.reservation.credit.dto.CreditReadyDto;
import com.study.reservation.credit.payment.dto.KakaoApproveResponseDto;
import com.study.reservation.credit.payment.dto.KakaoCancelResponseDto;
import com.study.reservation.credit.payment.dto.KakaoReadyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    @Value("${payment.admin-key}")
    private String adminKey;

    @Value("${payment.cid}")
    private String cid;

    @Value("${payment.success}")
    private String successUrl;

    @Value("${payment.fail}")
    private String failUrl;

    @Value("${payment.cancel}")
    private String cancelUrl;

    private KakaoReadyResponseDto kakaoReadyResponseDto;
    private CreditReadyDto creditReadyDto;

    //결제 준비단계
    public KakaoReadyResponseDto readyToKakaoPay(CreditReadyDto readyDto) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("cid", cid);
        parameters.add("partner_order_id", String.valueOf(readyDto.getOrderId()));
        parameters.add("partner_user_id", String.valueOf(readyDto.getMemberId()));
        parameters.add("item_name", readyDto.getProductName());
        parameters.add("item_code", String.valueOf(readyDto.getRoomId()));
        parameters.add("quantity", "1");
        parameters.add("total_amount", Integer.toString(readyDto.getTotalPrice()));
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", successUrl); // 성공 시 redirect url
        parameters.add("cancel_url", cancelUrl); // 취소 시 redirect url
        parameters.add("fail_url", failUrl); // 실패 시 redirect url

        HttpHeaders headers = getHeaders();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        kakaoReadyResponseDto = restTemplate.postForObject(url, requestEntity, KakaoReadyResponseDto.class);

        creditReadyDto = readyDto;

        return kakaoReadyResponseDto;
    }

    //결제 승인단계
    public KakaoApproveResponseDto approveToKakaoPay(String pgToken) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("cid", cid);
        parameters.add("tid", kakaoReadyResponseDto.getTid());
        parameters.add("partner_order_id", String.valueOf(creditReadyDto.getOrderId()));
        parameters.add("partner_user_id", String.valueOf(creditReadyDto.getMemberId()));
        parameters.add("pg_token", pgToken);

        HttpHeaders headers = getHeaders();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        log.info("header : {}", requestEntity.getHeaders().toString());
        log.info("body : {}", requestEntity.getBody().toString());

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        KakaoApproveResponseDto kakaoApproveResponseDto = restTemplate.postForObject(url, requestEntity, KakaoApproveResponseDto.class);

        return kakaoApproveResponseDto;
    }

    //결제 환불단계
    public KakaoCancelResponseDto refundToKakaoPay(CreditCancelDto creditCancelDto) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("cid", cid);
        parameters.add("tid", creditCancelDto.getTid());
        parameters.add("cancel_amount", creditCancelDto.getCancelAmount());
        parameters.add("cancel_tax_free_amount", "0");

        HttpHeaders headers = getHeaders();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";
        KakaoCancelResponseDto kakaoCancelResponseDto = restTemplate.postForObject(url, requestEntity, KakaoCancelResponseDto.class);

        return kakaoCancelResponseDto;
    }

    //헤더 설정
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Accept", "application/json");

        return headers;
    }

}
