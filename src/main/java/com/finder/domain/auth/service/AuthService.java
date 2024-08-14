package com.finder.domain.auth.service;

import com.finder.domain.auth.dto.request.LoginRequest;
import com.finder.domain.auth.dto.request.ReissueRequest;
import com.finder.domain.auth.dto.request.SignUpRequest;
import com.finder.global.security.jwt.dto.Jwt;

public interface AuthService {
    void signup(SignUpRequest request);

    Jwt login(LoginRequest request);

    Jwt reissue(ReissueRequest request);
}
