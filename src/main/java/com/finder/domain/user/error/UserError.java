package com.finder.domain.user.error;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserError implements CustomError {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "User already exists"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Username already exists"),
    USER_SECESSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "User secession failed"),
    ;

    private final HttpStatus status;
    private final String message;
}
