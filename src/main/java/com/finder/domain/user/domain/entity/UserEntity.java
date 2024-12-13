package com.finder.domain.user.domain.entity;

import com.finder.domain.user.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_image", nullable = true)
    private String profileImageURL;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}

// TODO: 유저 프로필 이미지 저장 작업 완료, 회원탈퇴 로직 완료

// TODO: S3 써서 이미지 연결해야함, 내 글, 내 댓글 보기 만들어야됌