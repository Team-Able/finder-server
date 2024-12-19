package com.finder.domain.user.dto.response;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.dto.response.ItemCommentResponse;

import java.util.List;
import java.util.stream.Collectors;

public record MyCommentsResponse(
        List<MyCommentResponse> myComments
) {
    public static MyCommentsResponse of(List<ItemCommentEntity> myComments) {
        return new MyCommentsResponse(myComments.stream().map(MyCommentResponse::of).filter(comment -> comment.parentId() == null).toList());
    }
}
