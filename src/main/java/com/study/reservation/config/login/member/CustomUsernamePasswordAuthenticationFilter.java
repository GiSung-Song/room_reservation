package com.study.reservation.config.login.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // /login 으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // application/json 타입의 데이터로 오는 요청
    private static final String USERNAME_KEY = "email"; //로그인 시 아이디를 이메일로 받기
    private static final String PASSWORD_KEY = "password"; //로그인 시 패스워드
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    private final ObjectMapper objectMapper;

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); //로그인 요청 처리
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("로그인 시도 필터 실행 - member");

        //content type이 null 이거나 application/json이 아닌 경우
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        // 입력 값을 UTF-8인코딩으로 string 복사
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        //Json -> Java Object 변환
        // {"email" : "이메일" , "password" : "비밀번호"}
        // <"email", "이메일">, <"password", "비밀번호">
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = usernamePasswordMap.get(USERNAME_KEY); //이메일
        String password = usernamePasswordMap.get(PASSWORD_KEY); //비밀번호

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password); //principal : email, credentials : password

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
