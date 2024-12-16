package com.finder.domain.item.repository;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findAllByStatus(ItemStatus status);

    List<ItemEntity> findAllByStatusOrderByCreatedAtDesc(ItemStatus status);

    List<ItemEntity> findAllByStatusOrderByViewCountDesc(ItemStatus status);

    void deleteAllByAuthorId(UUID authorId);
}
