package com.finder.domain.user.service.impl;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.domain.user.dto.response.UserResponse;
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
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityHolder securityHolder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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

        return itemList.stream().map(ItemResponse::of).toList();
    }
}