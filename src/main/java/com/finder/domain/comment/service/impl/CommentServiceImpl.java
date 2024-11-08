package com.finder.domain.comment.service.impl;

import com.finder.domain.comment.domain.entity.CoCommentEntity;
import com.finder.domain.comment.domain.entity.CommentEntity;
import com.finder.domain.comment.dto.request.CoCommentRequest;
import com.finder.domain.comment.dto.request.CommentRequest;
import com.finder.domain.comment.dto.request.PatchRequest;
import com.finder.domain.comment.dto.response.CoCommentResponse;
import com.finder.domain.comment.dto.response.CommentResponse;
import com.finder.domain.comment.exception.impl.ParentCommentNotFound;
import com.finder.domain.comment.repository.CoCommentRepository;
import com.finder.domain.comment.repository.CommentRepository;
import com.finder.domain.comment.service.CommentService;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final CoCommentRepository coCommentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse createComment(Long itemId, CommentRequest request) {
        ItemEntity item = itemRepository.findById(itemId).get();
        UUID postAuthor = item.getAuthor().getId();
        CommentEntity commentEntity = CommentEntity.builder()
                .author(request.author())
                .itemId(itemId)
                .content(request.content())
                .authenticationUsers(null)
                .childComments(null)
                .build();
        commentEntity.getAuthenticationUsers().add(userRepository.findById(postAuthor).get());
        commentEntity.getAuthenticationUsers().add(userRepository.findById(request.author()).get());
        item.getComments().add(commentEntity);
        itemRepository.save(item);
        return CommentResponse.of(commentRepository.save(commentEntity));
    }

    @Override
    public CoCommentResponse createCoComment(CoCommentRequest request) {
        CommentEntity parent = commentRepository.findById(request.parent()).orElseThrow(
                () -> ParentCommentNotFound.INSTANCE
        );
        CoCommentEntity coCommentEntity = CoCommentEntity.builder()
                .author(request.author())
                .content(request.content())
                .parent(parent)
                .build();
        parent.getChildComments().add(coCommentEntity);
        commentRepository.save(parent);
        return CoCommentResponse.of(coCommentRepository.save(coCommentEntity));
    }

    @Override
    public CommentResponse updateComment(PatchRequest request) {
        CommentEntity commentEntity = commentRepository.findById(request.commentId()).get();
        commentEntity.setContent(request.content());
        return CommentResponse.of(commentRepository.save(commentEntity));
    }

    @Override
    public CoCommentResponse updateCoComment(PatchRequest request) {
        CoCommentEntity commentEntity = coCommentRepository.findById(request.commentId()).get();
        commentEntity.setContent(request.content());
        return CoCommentResponse.of(coCommentRepository.save(commentEntity));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCoComment(Long commentId) {
        coCommentRepository.deleteById(commentId);
    }
}
