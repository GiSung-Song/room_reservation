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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter - doFilterInternal 필터 호출");
        log.info("요청 URI : {}", request.getRequestURI());
        log.info("요청 Method : {}", request.getMethod());

        // /login 요청이 오면
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            //다음 filter 실행
            filterChain.doFilter(request, response);
            return; //이후 필터 진행 막기
        }

        String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);
        String refreshToken = jwtTokenProvider.extractRefreshToken(request);

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
