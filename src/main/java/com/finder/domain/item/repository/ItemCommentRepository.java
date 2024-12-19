package com.finder.domain.item.repository;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemCommentRepository extends JpaRepository<ItemCommentEntity, Long> {
    List<ItemCommentEntity> findAllDistinctByAuthor(UserEntity author);
}
