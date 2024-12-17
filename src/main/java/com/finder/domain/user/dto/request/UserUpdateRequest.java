package com.finder.domain.user.dto.request;

public record UserUpdateRequest(
        String profileImageURL,
        String username
) {
}
