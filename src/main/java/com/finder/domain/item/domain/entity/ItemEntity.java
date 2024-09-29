package com.finder.domain.item.domain.entity;

import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    public void increaseViewCount() {
        this.viewCount++;
    }
}
