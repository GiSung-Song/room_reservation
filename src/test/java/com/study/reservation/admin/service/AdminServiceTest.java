package com.study.reservation.admin.service;

import com.study.reservation.admin.dto.AdminSignUpDto;
import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Spy
    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminSignUpDto makeDto() {
        AdminSignUpDto adminSignUpDto = new AdminSignUpDto();
        adminSignUpDto.setOwner("테스트");
        adminSignUpDto.setPassword("12341234");
        adminSignUpDto.setCompanyNumber("1234123412");
        adminSignUpDto.setPhoneNumber("01012341234");
        adminSignUpDto.setOpenDate("20240401");
        adminSignUpDto.setAccountNumber("12341234123412");

        return adminSignUpDto;
    }

    private Admin toEntity(AdminSignUpDto adminSignUpDto) {
        return Admin.builder()
                .owner(adminSignUpDto.getOwner())
                .password(adminSignUpDto.getPassword())
                .companyNumber(adminSignUpDto.getCompanyNumber())
                .phoneNumber(adminSignUpDto.getPhoneNumber())
                .openDate(adminSignUpDto.getOpenDate())
                .accountNumber(adminSignUpDto.getAccountNumber())
                .build();
    }

    @Nested
    class join {

        @Test
        @DisplayName("회원가입 성공 테스트")
        public void 회원가입_성공_테스트() {
            AdminSignUpDto adminSignUpDto = makeDto();
            Admin admin = toEntity(adminSignUpDto);

            Long fakeId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeId);

            given(adminRepository.save(any())).willReturn(admin);
            given(adminRepository.findById(fakeId)).willReturn(Optional.ofNullable(admin));

            Long savedId = adminService.signUp(adminSignUpDto);

            Admin findAdmin = adminRepository.findById(savedId).get();

            assertEquals(findAdmin.getOwner(), adminSignUpDto.getOwner());
            assertEquals(findAdmin.getCompanyNumber(), adminSignUpDto.getCompanyNumber());
        }
    }
}