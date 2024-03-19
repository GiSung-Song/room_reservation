package com.study.reservation.config.jwt.filter;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.JwtTokenProvider;
import com.study.reservation.config.redis.RedisRepository;
import com.study.reservation.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> NO_CHECK_URL = List.of(
            "/login"
    );

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("shouldNotFilter 필터 실행");

        String path = request.getRequestURI();

        /**
         * /** 로 끝나면 앞의 url 로 시작하는 패턴 match
         * 나머지는 일치하는 글자 match
         */
        return NO_CHECK_URL.stream().anyMatch(url -> {
            if (url.endsWith("/**")) {
                return path.startsWith(url.substring(0, url.length() - 3));
            } else {
                return url.equalsIgnoreCase(path);
            }
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter - doFilterInternal 필터 호출");
        log.info("요청 URI : {}", request.getRequestURI());
        log.info("요청 Method : {}", request.getMethod());

        String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);
        String refreshToken = jwtTokenProvider.extractRefreshToken(request).orElse(null);

        //액세스 토큰이 유효한 경우
        if (accessToken != null && jwtTokenProvider.isTokenValid(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (accessToken != null && !jwtTokenProvider.isTokenValid(accessToken)){

            //액세스 토큰이 있지만 유효하지 않고, refreshToken이 있는 경우 accessToken 재발급
            if (refreshToken != null && jwtTokenProvider.isTokenValid(refreshToken)) {
                RefreshToken refresh = redisRepository.findById(refreshToken).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
                String savedRefreshToken = refresh.getRefreshToken();
                String email = refresh.getEmail();

                //저장된 refreshToken이 없는 경우 throw
                if (savedRefreshToken == null) {
                    throw new CustomException(ErrorCode.NOT_VALID_ERROR);
                }

                //저장된 refreshToken과 request에 들어온 refreshToken이 다른 경우 throw
                if (!savedRefreshToken.equals(refreshToken)) {
                    throw new CustomException(ErrorCode.NOT_VALID_ERROR);
                }

                if (email == null || !memberRepository.findByEmail(email).isPresent()) {
                    throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
                }

                //새로운 AccessToken 생성 및 발급
                String newAccessToken = jwtTokenProvider.createAccessToken(email);
                jwtTokenProvider.sendAccessToken(response, newAccessToken);

                //Authentication 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
