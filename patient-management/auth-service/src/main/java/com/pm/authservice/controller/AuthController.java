package com.pm.authservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pm.authservice.common.AuthServiceBaseController;
import com.pm.authservice.dto.request.LoginRequest;
import com.pm.authservice.dto.response.ApiResponse;
import com.pm.authservice.dto.response.LoginResponse;
import com.pm.authservice.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-CONTROLLER")
public class AuthController extends AuthServiceBaseController {

    private final AuthService authService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {

        Optional<String> token = authService.authenticate(req);

        if (token.isEmpty()) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Login denied", null);
        }

        return createSuccessResponse(new LoginResponse(token.get()));
    }

    @Operation(summary = "Validate+ token on user login")
    @PostMapping("/validate")
    public ApiResponse<String> validateToken(@RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Request header invalid", null);
        }

        return authService.validateToken(authHeader.substring(7))
                ? createSuccessResponse(null)
                : createErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Token invalid", null);

    }

}
