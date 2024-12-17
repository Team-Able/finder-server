package com.finder.domain.item.dto.response;

import com.finder.domain.user.domain.entity.UserEntity;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.UUID;

public record ItemAuthorResponse(
        UUID id,
        String profileImageURL,
        String username
) {
    public static ItemAuthorResponse of(UserEntity user) {
        return new ItemAuthorResponse(user.getId(), user.getProfileImageURL(), user.getUsername());
    }
}
