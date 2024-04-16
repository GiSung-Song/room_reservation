package com.study.reservation.order.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    EMPTY_ORDER("예약없음"),
    READY_CREDIT("결제단계"),
    CONFIRM_ORDER("예약완료"),
    CANCEL_ORDER("예약취소");

    private final String korean;
}
