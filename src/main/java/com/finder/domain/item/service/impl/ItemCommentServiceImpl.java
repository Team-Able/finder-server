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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCommentServiceImpl implements ItemCommentService {
    private final ItemRepository itemRepository;
    private final ItemCommentRepository itemCommentRepository;
    private final SecurityHolder securityHolder;


    @Override
    @Transactional
    public ItemCommentResponse createItemComment(Long itemId, ItemCommentCreateRequest request) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ItemError.ITEM_NOT_FOUND));
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
    public ItemCommentResponse updateItemComment(Long itemId, Long commentId, ItemCommentUpdateRequest request) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ItemError.ITEM_NOT_FOUND));
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
        // 아이템과 댓글을 찾습니다.
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ItemError.ITEM_NOT_FOUND)
        );
        ItemCommentEntity comment = item.getComments().stream()
                .filter(itemCommentEntity -> itemCommentEntity.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ItemCommentError.ITEM_COMMENT_NOT_FOUND));

        // 작성자가 맞는지 확인
        if (!comment.getAuthor().equals(securityHolder.getPrincipal())) {
            throw new CustomException(ItemCommentError.ITEM_COMMENT_NOT_DELETABLE);
        }

        // 자식 댓글 삭제
        deleteChildComments(comment);

        // 부모 댓글에서 자식 댓글 제거
        if (comment.getParent() != null) {
            comment.getParent().getChildren().remove(comment);
        }

        // 아이템에서 댓글 제거
        item.getComments().remove(comment);

        // 댓글 삭제
        itemCommentRepository.delete(comment);
    }

    // 자식 댓글을 재귀적으로 삭제하는 메서드
    private void deleteChildComments(ItemCommentEntity comment) {
        // 자식 댓글이 있을 경우, 자식 댓글들을 먼저 삭제합니다.
        if (comment.getChildren() != null) {
            for (ItemCommentEntity child : new ArrayList<>(comment.getChildren())) {
                deleteChildComments(child);  // 자식 댓글이 또 자식을 가질 수 있으므로 재귀적으로 삭제합니다.
            }
        }
    }




//
//        ItemCommentEntity commentParent = comment.getParent();
//        if (comment.getParent() != null) {
//            comment.getParent().getChildren().remove(comment);
//            comment.setParent(null);
//        }
//        while (!comment.getChildren().isEmpty()) {
//            for (ItemCommentEntity child : comment.getChildren()) {
//                deleteItemComment(itemId, child.getId());
//            }
//        }
//        if (comment.getChildren().isEmpty()) {
//            item.getComments().remove(comment);
//            comment.setParent(null);
//            itemCommentRepository.delete(comment);
//            assert commentParent != null;
//            deleteItemComment(itemId, commentParent.getId());
//        } else {
//            item.getComments().remove(comment);
//            comment.setParent(null);
//            itemCommentRepository.delete(comment);
//        }
}
