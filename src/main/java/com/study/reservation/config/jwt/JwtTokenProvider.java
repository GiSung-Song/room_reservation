package com.study.reservation.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.reservation.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtTokenProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REFRESH_COOKIE_NAME = "RefreshCookie";

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationTime;

    @Value("${jwt.access.header}")
    private String accessHeader;

    //AccessToken 생성
    public String createAccessToken(String email) {
        log.info("AccessToken 생성");

        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationTime))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    //RefreshToken 생성
    public String createRefreshToken() {
        log.info("Refresh Token 생성");

        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationTime))
                .sign(Algorithm.HMAC512(secretKey));
    }

    //refreshToken 쿠키만들기
    public Cookie createRefreshTokenCookie(String refreshToken) {

        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpirationTime.intValue());

        return cookie;
    }

    //AccessToken 헤더에 전송
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        log.info("AccessToken 전송 : {}", accessToken);

        response.setStatus(HttpServletResponse.SC_OK); //200 성공
        response.setHeader(accessHeader, TOKEN_PREFIX + accessToken); //ex) AccessToken : BEARER fdjiaopjfdipoas
    }

    //AccessToken + RefreshToken 전송 및 RefreshToken Redis 저장
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        log.info("AccessToken + RefreshToken 전송");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, TOKEN_PREFIX + accessToken);

        //리프레시 토큰 쿠키 세팅
        response.addCookie(createRefreshTokenCookie(refreshToken));

        //리프레시 토큰 저장 (액세스 토큰 key, 리프레시 토큰 value)

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration time = Duration.ofSeconds(refreshTokenExpirationTime);
        valueOperations.set(accessToken, refreshToken, time);
    }

    //AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        log.info("AccessToken 추출");

        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(TOKEN_PREFIX))
                .map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    //RefreshToken 추출
    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(REFRESH_COOKIE_NAME))
                    .findFirst()
                    .map(c -> c.getValue())
                    .orElse(null);
        } else {
            return null;
        }

    }

    //AccessToken이 유효한지 검증하고 로그인ID 가져오기
    public Optional<String> extractEmail(String accessToken) {
        log.info("AccessToken 유효한지 검사 후 로그인 ID 가져오기");

        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.info("액셋스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    //Token이 유효한지 검증
    public boolean isTokenValid(String accessToken) {
        log.info("유효한 토큰인지 검증");

        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken);

            log.info("유효한 토큰");
            return true;
        } catch (Exception e) {
            log.info("유효하지 않은 토큰 {}", e.getMessage());
            return false;
        }
    }
}
