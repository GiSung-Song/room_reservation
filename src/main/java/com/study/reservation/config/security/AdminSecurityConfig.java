package com.study.reservation.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reservation.config.jwt.JwtTokenProvider;
import com.study.reservation.config.jwt.filter.admin.JwtAdminAuthenticationFilter;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.login.LoginFailureHandler;
import com.study.reservation.config.login.LoginService;
import com.study.reservation.config.login.admin.AdminCustomUsernamePasswordAuthenticationFilter;
import com.study.reservation.config.login.admin.AdminLoginSuccessHandler;
import com.study.reservation.config.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)
public class AdminSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final RedisRepository redisRepository;
    private final AdminRepository adminRepository;

    @Bean
    public JwtAdminAuthenticationFilter jwtAdminAuthenticationFilter() {
        return new JwtAdminAuthenticationFilter(jwtTokenProvider, adminRepository, redisRepository);
    }

    @Bean
    public AdminLoginSuccessHandler adminLoginSuccessHandler() {
        return new AdminLoginSuccessHandler(jwtTokenProvider);
    }

    @Bean
    public LoginFailureHandler adminLoginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AdminCustomUsernamePasswordAuthenticationFilter adminCustomUsernamePasswordAuthenticationFilter() {
        AdminCustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new AdminCustomUsernamePasswordAuthenticationFilter(objectMapper);

        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(adminAuthenticationManager());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(adminLoginSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(adminLoginFailureHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager adminAuthenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(adminPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(loginService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder adminPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers(HttpMethod.POST, "/admin/login", "/admin/join", "/join", "/login")
                    .requestMatchers("/v3/**", "/swagger*/**");
        };
    }

    @Bean
    public SecurityFilterChain securityAdminFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/admin/**")
                .addFilterAfter(adminCustomUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAdminAuthenticationFilter(), AdminCustomUsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("RefreshCookie"))
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/join").permitAll());

        return httpSecurity.build();
    }

}
