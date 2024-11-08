package com.finder.domain.comment.exception;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentException implements CustomError {
    Parent_Comment_Not_Found(HttpStatus.NOT_FOUND, "Parent comment not found");

    private final HttpStatus status;
    private final String message;
}
