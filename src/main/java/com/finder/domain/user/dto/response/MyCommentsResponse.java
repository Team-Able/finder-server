package com.finder.domain.user.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;

import java.util.List;

public record MyCommentsResponse(
        List<MyCommentResponse> myComments
) {
    public static MyCommentsResponse of(List<ItemCommentEntity> myComments) {
        return new MyCommentsResponse(myComments.stream().map(MyCommentResponse::of).filter(comment -> comment.parentId() == null).toList());
    }
}
