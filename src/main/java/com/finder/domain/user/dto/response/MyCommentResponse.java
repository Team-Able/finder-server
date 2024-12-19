package com.finder.domain.user.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.response.ItemAuthorResponse;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.security.holder.SecurityHolder;
import lombok.Getter;

import java.util.List;

public record MyCommentResponse(
        Long commentId,
        ItemAuthorResponse author,
        Long itemId,
        String content,
        Long parentId,
        List<MyCommentResponse> children
        ) {
    public static MyCommentResponse of(ItemCommentEntity comment) {
        return new MyCommentResponse(
                comment.getId(),
                ItemAuthorResponse.of(comment.getAuthor()),
                comment.getItem().getId(),
                comment.getContent(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getChildren().stream().map(MyCommentResponse::of).toList()
        );
    }
}
