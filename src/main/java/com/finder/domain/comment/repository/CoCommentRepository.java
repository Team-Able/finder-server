package com.finder.domain.comment.repository;

import com.finder.domain.comment.domain.entity.CoCommentEntity;
import com.finder.domain.comment.dto.response.CoCommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoCommentRepository extends JpaRepository<CoCommentEntity, Long> {
    List<CoCommentResponse> findAllByParentOrderByCreatedAtDesc(Long id);
}
