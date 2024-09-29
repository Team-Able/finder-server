package com.finder.domain.auth.service.impl;

import com.finder.domain.auth.dto.request.LoginRequest;
import com.finder.domain.auth.dto.request.ReissueRequest;
import com.finder.domain.auth.dto.request.SignUpRequest;
import com.finder.domain.auth.repository.RefreshTokenRepository;
import com.finder.domain.auth.service.AuthService;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.domain.user.domain.enums.UserRole;
import com.finder.domain.user.error.UserError;
import com.finder.domain.user.repository.UserRepository;
import com.finder.global.exception.CustomException;
import com.finder.global.security.jwt.dto.Jwt;
import com.finder.global.security.jwt.enums.JwtType;
import com.finder.global.security.jwt.error.JwtError;
import com.finder.global.security.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void signup(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new CustomException(UserError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(UserError.USER_ALREADY_EXISTS);
        }

        UserEntity user = UserEntity.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .username(request.username())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public Jwt login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(UserError.INVALID_PASSWORD);
        }

        Jwt token = jwtProvider.generateToken(user);

        refreshTokenRepository.save(user.getEmail(), token.refreshToken());

        return token;
    }

    @Override
    public Jwt reissue(ReissueRequest request) {
        if (jwtProvider.getType(request.refreshToken()) != JwtType.REFRESH)
            throw new CustomException(JwtError.INVALID_TOKEN);

        String email = jwtProvider.getEmail(request.refreshToken());

        if (!refreshTokenRepository.existsByEmail(email))
            throw new CustomException(JwtError.INVALID_TOKEN);

        String refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(JwtError.INVALID_TOKEN));

        if (!refreshToken.equals(request.refreshToken()))
            throw new CustomException(JwtError.INVALID_TOKEN);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        Jwt token = jwtProvider.generateToken(user);

        refreshTokenRepository.save(email, token.refreshToken());

        return token;
    }
}
