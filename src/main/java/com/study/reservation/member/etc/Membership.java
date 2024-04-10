package com.study.reservation.member.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Membership {
    BRONZE("브론즈", 0.01),
    SILVER("실버", 0.02),
    GOLD("골드", 0.03),
    PLATINUM("플래티넘", 0.05);

    private final String korean;
    private final double pointRate;
}