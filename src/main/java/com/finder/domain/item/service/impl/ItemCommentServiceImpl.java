package com.finder.domain.item.service.impl;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.request.ItemCommentCreateRequest;
import com.finder.domain.item.dto.request.ItemCommentUpdateRequest;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.item.error.ItemCommentError;
import com.finder.domain.item.error.ItemError;
import com.finder.domain.item.repository.ItemCommentRepository;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.item.service.ItemCommentService;
import com.finder.global.exception.CustomException;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ItemCommentServiceImpl implements ItemCommentService {
    private final ItemRepository itemRepository;
    private final ItemCommentRepository itemCommentRepository;
    private final SecurityHolder securityHolder;


    @Override
    @Transactional
    public ItemCommentResponse createItemComment(ItemCommentCreateRequest request) {
        ItemEntity item = itemRepository.findById(request.itemId()).orElseThrow(() -> new CustomException(ItemError.ITEM_NOT_FOUND));
        ItemCommentEntity parent = null;
        if (request.parentId() != null) {
            parent = itemCommentRepository.findById(request.parentId()).orElseThrow(
                    () -> new CustomException(ItemCommentError.COMMENT_PARRENT_NOT_FOUND));
        }

        ItemCommentEntity itemComment = ItemCommentEntity.builder()
                .author(securityHolder.getPrincipal())
                .item(item)
                .content(request.content())
                .parent(parent)
                .build();

        return ItemCommentResponse.of(itemCommentRepository.save(itemComment));
    }

    @Override
    public ItemCommentResponse updateItemComment(Long commentId, ItemCommentUpdateRequest request) {
        ItemEntity item = itemRepository.findById(request.itemId()).orElseThrow(() -> new CustomException(ItemError.ITEM_NOT_FOUND));
        ItemCommentEntity comment = item.getComments().stream().filter(itemCommentEntity -> itemCommentEntity.getId().equals(commentId)).findFirst().orElseThrow(() -> new CustomException(ItemCommentError.ITEM_COMMENT_NOT_FOUND));
        if (!comment.getAuthor().equals(securityHolder.getPrincipal())) {
            throw new CustomException(ItemCommentError.ITEM_COMMENT_NOT_DELETABLE);
        }
        comment.setContent(request.content());

        return ItemCommentResponse.of(itemCommentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteItemComment(Long itemId, Long commentId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ItemError.ITEM_NOT_FOUND)
        );
        ItemCommentEntity comment = item.getComments().stream()
                .filter(itemCommentEntity -> itemCommentEntity.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ItemCommentError.ITEM_COMMENT_NOT_FOUND));

        if (!comment.getAuthor().equals(securityHolder.getPrincipal())) {
            throw new CustomException(ItemCommentError.ITEM_COMMENT_NOT_DELETABLE);
        }

        if (comment.getParent() != null) {
            comment.getParent().getChildren().remove(comment);
        }

        item.getComments().remove(comment);

        itemCommentRepository.delete(comment);
    }
}
