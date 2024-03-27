package com.study.reservation.config.jwt.filter;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RefreshToken {

    @Id
    private String refreshToken;
    private String id;

    public RefreshToken(String refreshToken, String id) {
        this.refreshToken = refreshToken;
        this.id = id;
    }
}
