package com.finder.domain.item.error;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemCommentError implements CustomError {
    ITEM_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),
    ITEM_COMMENT_NOT_DELETABLE(HttpStatus.BAD_REQUEST, "Comment not deletable"),
    ;


    private final HttpStatus status;
    private final String message;
}
