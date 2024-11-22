package com.finder.domain.item.repository;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCommentRepository extends JpaRepository<ItemCommentEntity, Long> {
}
