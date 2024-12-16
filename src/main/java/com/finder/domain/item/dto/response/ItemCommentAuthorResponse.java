package com.finder.domain.item.dto.response;

import com.finder.domain.user.domain.entity.UserEntity;

import java.util.UUID;

public record ItemCommentAuthorResponse(
        UUID id,
        String profileImageUrl,
        String username
) {
    public static ItemCommentAuthorResponse of(UserEntity user) {
        return new ItemCommentAuthorResponse(user.getId(), user.getProfileImageURL() ,user.getUsername());
    }
}
