package com.finder.global.security.holder;

import com.finder.domain.user.domain.entity.UserEntity;

public interface SecurityHolder {
    UserEntity getPrincipal();
}
