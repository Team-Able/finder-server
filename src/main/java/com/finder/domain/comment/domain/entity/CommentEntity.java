package com.finder.domain.comment.domain.entity;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Table(name = "Comments")
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author",nullable = false)
    private UUID author; //댓글 쓴 사람 id

    @Column(name = "itemId",nullable = false)
    private Long itemId; //물품id

    @Column(name = "content", nullable = false)
    private String content; //내용

    @Column(name = "authentication_users", nullable = false)
    @OneToMany
    private List<UserEntity> authenticationUsers;

    @Column(name = "child", nullable = true)
    @OneToMany
    private List<CoCommentEntity> childComments; //하위 대댓글 리스트
}


