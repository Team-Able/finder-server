package com.finder.domain.user.dto.response;

import com.finder.domain.user.domain.entity.UserEntity;

public record UserResponse(
        String profileImageUrl,
        String username,
        String email
) {
    public static UserResponse of(UserEntity user) {
        return new UserResponse(user.getProfileImageURL() ,user.getUsername(), user.getEmail());
    }
}