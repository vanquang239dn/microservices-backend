package com.pm.authservice.service;

import java.util.Optional;

import com.pm.authservice.dto.request.LoginRequest;

public interface AuthService {

    public Optional<String> authenticate(LoginRequest req);

    public boolean validateToken(String substring);

}
