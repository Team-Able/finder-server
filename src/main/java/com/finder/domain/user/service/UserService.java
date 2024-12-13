package com.finder.domain.user.service;

import com.finder.domain.user.dto.response.UserResponse;

public interface UserService {
    UserResponse getMe();
    void secession();
}
