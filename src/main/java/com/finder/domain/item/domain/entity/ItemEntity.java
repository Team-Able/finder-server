package com.finder.domain.item.domain.entity;

import com.finder.domain.comment.domain.entity.CommentEntity;
import com.finder.domain.comment.dto.response.CommentResponse;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
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

    @OneToMany
    private List<CommentEntity> comments;

    public void increaseViewCount() {
        this.viewCount++;
    }
}
