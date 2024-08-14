package com.finder.global.security.holder.impl;

import com.finder.domain.user.domain.entity.UserEntity;
import com.finder.domain.user.error.UserError;
import com.finder.domain.user.repository.UserRepository;
import com.finder.global.exception.CustomException;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHolderImpl implements SecurityHolder {
    private final UserRepository userRepository;

    @Override
    public UserEntity getPrincipal() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
    }
}
