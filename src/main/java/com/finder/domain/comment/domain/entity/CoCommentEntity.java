package com.finder.domain.comment.domain.entity;

import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "CoComments")
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CoCommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author",nullable = false)
    private UUID author; //댓글 쓴 사람 id

    @Column(name = "content", nullable = false)
    private String content; //내용

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", nullable = false)
    private CommentEntity parent; //상위 댓글 id
}
