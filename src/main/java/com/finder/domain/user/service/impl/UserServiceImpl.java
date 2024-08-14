package com.finder.domain.user.service.impl;

import com.finder.domain.user.dto.response.UserResponse;
import com.finder.domain.user.service.UserService;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityHolder securityHolder;

    @Override
    public UserResponse getMe() {
        return UserResponse.of(securityHolder.getPrincipal());
    }
}
