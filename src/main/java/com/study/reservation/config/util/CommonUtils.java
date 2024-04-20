package com.study.reservation.config.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonUtils {

    public String getAuthUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       log.info("authentication : {}", authentication);

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        return username;
    }

}
