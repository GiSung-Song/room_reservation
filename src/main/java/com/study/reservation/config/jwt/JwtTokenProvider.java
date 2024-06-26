package com.study.reservation.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.filter.RefreshToken;
import com.study.reservation.config.redis.RedisRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
    private static final String COMPANY_NUMBER_CLAIM = "companyNumber";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REFRESH_COOKIE_NAME = "RefreshCookie";

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;

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

    //AccessToken 생성
    public String createAdminAccessToken(String companyNumber) {
        log.info("AccessToken 생성");

        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationTime))
                .withClaim(COMPANY_NUMBER_CLAIM, companyNumber)
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

    public void createAndSendToken(HttpServletResponse response, String id, boolean memberYn) {
        log.info("createAndSendToken 실행");

        String accessToken = "";

        if (memberYn == true) {
            accessToken = createAccessToken(id);
        } else {
            accessToken = createAdminAccessToken(id);
        }

        String refreshToken = createRefreshToken();

        RefreshToken refresh = new RefreshToken(refreshToken, id);

        //response Header 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, TOKEN_PREFIX + accessToken);

        //리프레시 토큰 쿠키 세팅
        response.addCookie(createRefreshTokenCookie(refreshToken));

        //RefreshToken 저장
        redisRepository.save(refresh);

        log.info("AccessToken : {}", accessToken);
        log.info("RefreshToken : {}", refreshToken);
        log.info("createAndSendToken 종료");
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
        log.info("RefreshToken 추출");

        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(REFRESH_COOKIE_NAME))
                    .findFirst()
                    .map(c -> c.getValue())
                    .orElse(null);
        } else {
            return null;
        }
    }

    public Authentication getAuthentication(String accessToken) {
        log.info("AccessToken -> Authentication 가져오기");

        String userEmail = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(EMAIL_CLAIM)
                .asString();

        log.info("userEmail : {}", userEmail);

        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        UserDetails userDetails = User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Authentication getAdminAuthentication(String accessToken) {
        log.info("AccessToken -> Authentication 가져오기");

        String companyNumber = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(COMPANY_NUMBER_CLAIM)
                .asString();

        log.info("companyNumber : {}", companyNumber);

        Admin admin = adminRepository.findByCompanyNumber(companyNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER));

        UserDetails userDetails = User.builder()
                .username(admin.getCompanyNumber())
                .password(admin.getPassword())
                .roles(admin.getRole().name())
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //Token이 유효한지 검증
    public boolean isTokenValid(String token) {
        log.info("유효한 토큰인지 검증");

        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);

            log.info("유효한 토큰");
            return true;
        } catch (Exception e) {
            log.info("유효하지 않은 토큰 {}", e.getMessage());
            return false;
        }
    }
}
