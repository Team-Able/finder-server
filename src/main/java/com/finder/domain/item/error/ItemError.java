package com.finder.domain.item.error;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemError implements CustomError {
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Item not found")
    ;

    private final HttpStatus status;
    private final String message;
}
