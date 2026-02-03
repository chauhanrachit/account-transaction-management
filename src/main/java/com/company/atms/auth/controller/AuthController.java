package com.company.atms.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.atms.auth.dto.LoginRequest;
import com.company.atms.auth.dto.LoginResponse;
import com.company.atms.auth.service.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
							        		new UsernamePasswordAuthenticationToken(
							        				request.getUsername(),
							        				request.getPassword()
							        		)
        								);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        LoginResponse response = LoginResponse.builder()
                .username(userDetails.getUsername())
                .role(userDetails.getUser().getRole().name())
                .message("Login successful")
                .build();

        return ResponseEntity.ok(response);
    }
}
