package com.finder.domain.user.service.impl;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.dto.response.ItemCommentResponse;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.user.dto.response.UserResponse;
import com.finder.domain.user.error.UserError;
import com.finder.domain.user.repository.UserRepository;
import com.finder.domain.user.service.UserService;
import com.finder.global.exception.CustomException;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityHolder securityHolder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserResponse getMe() {
        return UserResponse.of(securityHolder.getPrincipal());
    }

    @Override
    public void secession() {
            itemRepository.deleteAllByAuthor(securityHolder.getPrincipal());
            userRepository.deleteById(securityHolder.getPrincipal().getId());
    }

    @Override
    public List<ItemResponse> getMyItems() {
        List<ItemEntity> itemList = itemRepository.findAll();

        return itemList.stream().map(ItemResponse::of).toList();
    }
}
