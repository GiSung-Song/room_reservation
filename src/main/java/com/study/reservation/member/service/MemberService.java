package com.study.reservation.member.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(MemberSignUpDto memberSignUpDto) {
        if (memberRepository.findByNickname(memberSignUpDto.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        if (memberRepository.findByEmail(memberSignUpDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        Member member = Member.builder()
                .name(memberSignUpDto.getName())
                .password(passwordEncoder.encode(memberSignUpDto.getPassword()))
                .nickname(memberSignUpDto.getNickname())
                .phoneNumber(memberSignUpDto.getPhoneNumber())
                .email(memberSignUpDto.getEmail())
                .build();

        return memberRepository.save(member).getId();
    }
}
