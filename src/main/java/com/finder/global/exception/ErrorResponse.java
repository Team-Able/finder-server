package com.finder.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    public ErrorResponse(CustomError error) {
        this.status = error.getStatus().value();
        this.code = ((Enum<?>) error).name();
        this.message = error.getMessage();
    }

    public static ErrorResponse of(CustomError error) {
        return new ErrorResponse(error);
    }

    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}
