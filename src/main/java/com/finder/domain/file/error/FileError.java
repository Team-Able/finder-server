package com.finder.domain.file.error;

import com.finder.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileError implements CustomError {
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;
}
