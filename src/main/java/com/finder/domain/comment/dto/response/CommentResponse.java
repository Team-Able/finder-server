package com.finder.domain.comment.dto.response;

import com.finder.domain.comment.domain.entity.CoCommentEntity;
import com.finder.domain.comment.domain.entity.CommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.user.domain.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CommentResponse(
        Long id,
        UUID author,
        Long itemId,
        String content,
        List<UserEntity> authenticationUsers,
        List<CoCommentEntity> childComments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
    public static CommentResponse of(CommentEntity comment) {
        return new CommentResponse(comment.getId(), comment.getAuthor(), comment.getItemId(), comment.getContent(), comment.getAuthenticationUsers() , comment.getChildComments(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
