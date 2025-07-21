package com.keakimleang.resourceserver.controller.rest;

import com.keakimleang.resourceserver.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MessagesController {

    @GetMapping("/messages")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('message.read') ")
    public String[] getMessages(@AuthenticationPrincipal Jwt jwt) {
        log.info("Getting messages for {}", jwt.toString());
        log.info("Getting messages for {}", jwt.getClaims().toString());
        Long userId = JwtService.getUserId(jwt);
        log.info("User ID: {}", userId);
        return new String[]{"Message 1", "Message 2", "Message 3"};
    }

}
