package com.finder.domain.auth.dto.request;

public record SignUpRequest(
        String username,
        String email,
        String password
) {
}
