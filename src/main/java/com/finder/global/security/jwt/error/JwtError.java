package com.finder.global.security.jwt.error;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtError implements CustomError {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token is expired.");

    private final HttpStatus status;
    private final String message;
}
