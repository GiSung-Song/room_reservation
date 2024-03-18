package com.study.reservation.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reservation.config.security.SecurityConfig;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    public MemberSignUpDto makeDto() {
        MemberSignUpDto memberSignUpDto = new MemberSignUpDto();
        memberSignUpDto.setEmail("test@test.com");
        memberSignUpDto.setName("이름");
        memberSignUpDto.setNickname("닉네임");
        memberSignUpDto.setPassword("12345");
        memberSignUpDto.setPhoneNumber("01012345678");

        return memberSignUpDto;
    }

    @DisplayName("회원가입 테스트")
    @Test
    void 회원가입_성공_테스트() throws Exception {
        given(memberService.signUp(makeDto())).willReturn(1L);

        mockMvc.perform(post("/member")
                        .secure(true)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(makeDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("회원가입 테스트")
    @Test
    void 회원가입_실패_테스트() throws Exception {
        MemberSignUpDto dto = makeDto();
        dto.setNickname("");
        given(memberService.signUp(dto)).willReturn(1L);

        mockMvc.perform(post("/member")
                        .secure(true)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}