package com.study.reservation.member.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Spy
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private MemberSignUpDto createDto() {
        MemberSignUpDto dto = new MemberSignUpDto();
        dto.setEmail("test@test.com");
        dto.setName("테스터");
        dto.setNickname("테스트");
        dto.setPhoneNumber("01012345678");
        dto.setPassword("test1234");

        return dto;
    }

    private Member toEntity(MemberSignUpDto dto) {
        Member member = Member.builder()
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .build();

        return member;
    }

    @Nested
    class join {

        @DisplayName("성공_테스트")
        @Test
        public void 회원가입_성공테스트() {
            //given
            MemberSignUpDto memberSignUpDto = createDto();
            Member member = toEntity(memberSignUpDto);

            Long fakeId = 1L;
            ReflectionTestUtils.setField(member, "id", fakeId);

            //mocking
            given(memberRepository.save(any())).willReturn(member);
            given(memberRepository.findById(fakeId)).willReturn(Optional.ofNullable(member));

            //when
            Long savedMemberId = memberService.signUp(memberSignUpDto);

            //then
            Member findMember = memberRepository.findById(savedMemberId).get();

            assertEquals(findMember.getId(), member.getId());
            assertEquals(findMember.getEmail(), member.getEmail());
            assertEquals(findMember.getNickname(), member.getNickname());
        }

        @DisplayName("실패 테스트")
        @Test
        void 회원가입_실패테스트() {

            //given
            MemberSignUpDto memberSignUpDto = createDto();

            //when
            doThrow(new CustomException(ErrorCode.DUPLICATE_EMAIL)).when(memberService).signUp(any(MemberSignUpDto.class));

            //then
            assertThrows(CustomException.class, () -> memberService.signUp(memberSignUpDto));
            verify(memberRepository, times(0)).save(any());
        }

    }

}