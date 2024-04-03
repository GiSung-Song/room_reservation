package com.study.reservation.product.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductType {
    HOTEL("호텔"),
    HOUSE("집"),
    MOTEL("모텔"),
    POOL_VILLA("풀빌라"),
    PARTY_ROOM("파티룸"),
    PENSION("펜션"),
    GUEST_HOUSE("게스트하우스"),
    RESORT("리조트");

    private final String korean;
}
