package com.study.reservation.config.login;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.admin.repository.AdminRepository;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("loadUserByUsername 실행 / username : {}", id);

        Member member = memberRepository.findByEmail(id).orElse(null);
        if (member != null) {
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    .roles(member.getRole().name())
                    .build();
        }

        Admin admin = adminRepository.findByCompanyNumber(id).orElse(null);
        if (admin != null) {
            return User.builder()
                    .username(admin.getCompanyNumber())
                    .password(admin.getPassword())
                    .roles(admin.getRole().name())
                    .build();
        }

        throw new CustomException(ErrorCode.NOT_VALID_ERROR);
    }
}
