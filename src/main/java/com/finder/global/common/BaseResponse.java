package com.finder.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse<T> {
    private final T data;
    private final int status;
    private final String message;
}
