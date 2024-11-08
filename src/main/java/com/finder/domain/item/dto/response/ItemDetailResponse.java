package com.finder.domain.item.dto.response;

import com.finder.domain.comment.domain.entity.CommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.user.domain.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ItemDetailResponse(
        Long id,
        String title,
        UserEntity author,
        String content,
        List<CommentEntity> comments,
        String imageUrl,
        ItemStatus status,
        ItemLocation location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ItemDetailResponse of(ItemEntity item, UUID userId) {
        List<CommentEntity> comments = item.getComments();
        int i = 0;
        List<CommentEntity> rcomments = comments.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        for (CommentEntity comment : rcomments) {
            if (!comment.getChildComments().contains(userId)) {
                comment.setAuthor(UUID.fromString("Secret"));
                comment.setContent("비밀댓글입니다.");
                comment.setChildComments(List.of());

                rcomments.set(i, comment);
            }
            i += 1;
        }

        return new ItemDetailResponse(item.getId(), item.getTitle(), item.getAuthor(), item.getContent(), rcomments, item.getImageUrl(), item.getStatus(), item.getLocation(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
