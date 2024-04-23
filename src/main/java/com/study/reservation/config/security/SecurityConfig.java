package com.study.reservation.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reservation.config.jwt.JwtTokenProvider;
import com.study.reservation.config.jwt.filter.admin.JwtAdminAuthenticationFilter;
import com.study.reservation.config.jwt.filter.member.JwtAuthenticationFilter;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.login.LoginFailureHandler;
import com.study.reservation.config.login.LoginService;
import com.study.reservation.config.login.admin.AdminCustomUsernamePasswordAuthenticationFilter;
import com.study.reservation.config.login.admin.AdminLoginSuccessHandler;
import com.study.reservation.config.login.member.CustomUsernamePasswordAuthenticationFilter;
import com.study.reservation.config.login.member.LoginSuccessHandler;
import com.study.reservation.config.redis.RedisRepository;
import com.study.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final RedisRepository redisRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    //로그인 성공 시 핸들러
    @Bean
    public AdminLoginSuccessHandler adminLoginSuccessHandler() {
        return new AdminLoginSuccessHandler(jwtTokenProvider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtTokenProvider);
    }

    //로그인 실패 시 핸들러
    @Bean
    public LoginFailureHandler adminLoginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    //
    @Bean
    public AdminCustomUsernamePasswordAuthenticationFilter adminCustomUsernamePasswordAuthenticationFilter() {
        AdminCustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new AdminCustomUsernamePasswordAuthenticationFilter(objectMapper);

        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(adminLoginSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(adminLoginFailureHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);

        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(loginService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers(HttpMethod.POST, "/admin/login", "/admin/join", "/join", "/login")
                    .requestMatchers(HttpMethod.GET, "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html");
        };
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/admin/room").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/admin/room/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/product").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/admin/product/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterAfter(adminCustomUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(new JwtAdminAuthenticationFilter(jwtTokenProvider, adminRepository, redisRepository), AdminCustomUsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("RefreshCookie"))
                .httpBasic(HttpBasicConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/orders").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/order/{id}").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/order/{id}").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/credit/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/join").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/member").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.PATCH, "/member").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/product/{id}/review").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/product/{id}/review").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/iamport/**").hasRole("MEMBER")
                        .anyRequest().authenticated()
                )
                .addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, memberRepository, redisRepository), CustomUsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("RefreshCookie"))
                .httpBasic(HttpBasicConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/product/{id}/review").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/{productId}/rooms/{roomId}").permitAll()
                        .anyRequest().permitAll()
                )
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("RefreshCookie"))
                .httpBasic(HttpBasicConfigurer::disable);

        return httpSecurity.build();
    }

}
