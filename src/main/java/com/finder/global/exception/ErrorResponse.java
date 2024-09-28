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

    public static ResponseEntity<ErrorResponse> of(CustomError error) {
        return ResponseEntity.status(error.getStatus()).body(new ErrorResponse(error));
    }
}
