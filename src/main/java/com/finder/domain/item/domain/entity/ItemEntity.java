package com.finder.domain.item.domain.entity;

import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "items")
@SuperBuilder
@RequiredArgsConstructor
public class ItemEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ItemStatus status;
}
