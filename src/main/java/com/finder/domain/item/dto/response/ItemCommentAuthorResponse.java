package com.finder.domain.item.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.user.domain.entity.UserEntity;

import java.util.UUID;

public record ItemCommentAuthorResponse(
        UUID id,
        String username
) {
    public static ItemCommentAuthorResponse of(UserEntity user) {
        return new ItemCommentAuthorResponse(user.getId(), user.getUsername());
    }
}
