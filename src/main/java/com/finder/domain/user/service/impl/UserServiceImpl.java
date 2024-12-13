package com.finder.domain.user.service.impl;

import com.finder.domain.user.dto.response.UserResponse;
import com.finder.domain.user.error.UserError;
import com.finder.domain.user.repository.UserRepository;
import com.finder.domain.user.service.UserService;
import com.finder.global.exception.CustomException;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityHolder securityHolder;
    private final UserRepository userRepository;

    @Override
    public UserResponse getMe() {
        return UserResponse.of(securityHolder.getPrincipal());
    }

    @Override
    public void secession() {
        try {
            userRepository.deleteById(securityHolder.getPrincipal().getId());
        } catch (Exception e) {
            throw new CustomException(UserError.USER_SECESSION_FAILED);
        }
    }
}
