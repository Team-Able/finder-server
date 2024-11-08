package com.finder.domain.comment.repository;

import com.finder.domain.comment.domain.entity.CommentEntity;
import com.finder.domain.comment.dto.response.CommentResponse;
import com.finder.domain.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentResponse> findAllByItemIdOrderByCreatedAtDesc(Long id);
}
