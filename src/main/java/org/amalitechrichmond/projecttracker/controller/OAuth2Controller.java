package org.amalitechrichmond.projecttracker.controller;

import org.amalitechrichmond.projecttracker.provider.JwtTokenProvider;
import org.amalitechrichmond.projecttracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Component
@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/success")
    public String oAuth2JwtSuccess() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return jwtTokenProvider.generateToken(auth.getName());
    }
}
