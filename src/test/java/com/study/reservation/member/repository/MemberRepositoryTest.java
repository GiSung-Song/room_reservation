package com.study.reservation.member.repository;

import com.study.reservation.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void save_member() {
        Member member = Member.builder()
                .email("test@test.test")
                .nickname("test")
                .name("test")
                .password("1234")
                .phoneNumber("1234")
                .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("이메일로 찾기")
    void findByEmail_test() {
        Member findMember = memberRepository.findByEmail("test@test.test").get();

        assertThat(findMember.getNickname()).isEqualTo("test");
    }

    @Test
    @DisplayName("닉네임으로 찾기")
    void findByNickname_test() {
        Member findMember = memberRepository.findByNickname("test").get();

        assertThat(findMember.getName()).isEqualTo("test");
    }

    @Nested
    class save {
        @Test
        @DisplayName("저장 테스트")
        void 성공_테스트() {
            Member member = Member.builder()
                    .email("test1@test.test")
                    .nickname("test1")
                    .name("test")
                    .password("1234")
                    .phoneNumber("1234")
                    .build();

            Member savedMember = memberRepository.save(member);

            assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        }

        @Test
        @DisplayName("저장 테스트")
        void 실패_테스트() {
            Member member = Member.builder()
                    .email("test@test.test")
                    .nickname("test")
                    .name("test")
                    .password("1234")
                    .phoneNumber("1234")
                    .build();

            assertThrows(Exception.class, () -> {
                memberRepository.save(member);
            });
        }
    }

}