package com.finder.domain.item.domain.entity;

import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@Table(name = "items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Embedded
    private ItemLocation location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Setter
    private ItemStatus status;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCommentEntity> comments = new ArrayList<>();

    public void increaseViewCount() {
        this.viewCount++;
    }
}
