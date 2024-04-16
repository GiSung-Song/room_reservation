package com.study.reservation.credit.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CreditStatus {

    CREDIT_READY("결제 준비"),
    CREDIT_FINISH("결제 완료"),
    CREDIT_REFUND("환불");

    private final String korean;
}
