package com.finder.domain.item.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Table(name = "item_comments")
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemCommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author; //댓글 쓴 사람 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",nullable = false)
    private ItemEntity item; //물품id

    @Column(name = "content", nullable = false)
    private String content; //내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private ItemCommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemCommentEntity> children;

    public List<ItemCommentEntity> getChildren() {
        return children != null ? children : Collections.emptyList();
    }


    public void removeChild(ItemCommentEntity child) {
        child.setParent(null); // 관계를 끊어줍니다.
        children.remove(child);
    }

}

