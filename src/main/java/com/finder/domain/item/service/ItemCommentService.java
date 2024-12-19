package com.finder.domain.item.service;

import com.finder.domain.item.dto.request.ItemCommentCreateRequest;
import com.finder.domain.item.dto.request.ItemCommentUpdateRequest;
import com.finder.domain.item.dto.response.ItemCommentResponse;

public interface ItemCommentService {
    ItemCommentResponse createItemComment(ItemCommentCreateRequest request);

    ItemCommentResponse updateItemComment(Long commentId, ItemCommentUpdateRequest request);

    void deleteItemComment(Long itemId, Long commentId);
}
