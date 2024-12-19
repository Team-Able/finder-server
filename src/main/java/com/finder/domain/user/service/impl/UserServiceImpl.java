package com.finder.domain.user.service.impl;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemCommentRepository;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.domain.user.dto.request.UserUpdateRequest;
import com.finder.domain.user.dto.response.*;
import com.finder.domain.user.error.UserError;
import com.finder.domain.user.repository.UserRepository;
import com.finder.domain.user.service.UserService;
import com.finder.global.exception.CustomException;
import com.finder.global.security.holder.SecurityHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityHolder securityHolder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemCommentRepository itemCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserResponse getMe() {
        return UserResponse.of(securityHolder.getPrincipal());
    }

    @Override
    public void secession() {
        UserEntity user = securityHolder.getPrincipal();

        userRepository.delete(user);
    }



    @Override
    public List<ItemResponse> getMyItems() {
        List<ItemEntity> itemList = itemRepository.findAll();

        return itemList.stream().filter(itemEntity -> itemEntity.getAuthor().equals(securityHolder.getPrincipal())).map(ItemResponse::of).toList();
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateRequest request) {
        UserEntity user = securityHolder.getPrincipal();

        if (request.profileImageURL() != null)
            user.setProfileImageURL(request.profileImageURL());
        if (request.username() != null)
            user.setUsername(request.username());

        userRepository.save(user);
    }

    @Override
    public RealFinalMyCommentsResponse getMyComments() {
        return RealFinalMyCommentsResponse.convertToCustomFormat(MyCommentsResponse.of(itemCommentRepository.findAllDistinctByAuthor(securityHolder.getPrincipal())));
    }
}