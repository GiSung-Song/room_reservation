package com.study.reservation.admin.service;

import com.study.reservation.admin.dto.AdminSignUpDto;
import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(AdminSignUpDto adminSignUpDto) {
        if (adminRepository.findByCompanyNumber(adminSignUpDto.getCompanyNumber()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_NUMBER);
        }

        String accountNumber = "";

        if (StringUtils.hasText(adminSignUpDto.getAccountNumber())) {
            accountNumber = adminSignUpDto.getAccountNumber();
        }

        Admin admin = Admin.builder()
                .owner(adminSignUpDto.getOwner())
                .password(passwordEncoder.encode(adminSignUpDto.getPassword()))
                .phoneNumber(adminSignUpDto.getPhoneNumber())
                .accountNumber(accountNumber)
                .companyNumber(adminSignUpDto.getCompanyNumber())
                .openDate(adminSignUpDto.getOpenDate())
                .build();

        return adminRepository.save(admin).getId();
    }

    public boolean existCompanyNumber(String companyNumber) {
        if (adminRepository.findByCompanyNumber(companyNumber).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

}
