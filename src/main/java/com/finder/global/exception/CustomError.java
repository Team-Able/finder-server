package com.finder.global.exception;

import org.springframework.http.HttpStatus;

public interface CustomError {
    String getMessage();

    HttpStatus getStatus();
}
