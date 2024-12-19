package com.finder.domain.user.service;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.user.dto.request.UserUpdateRequest;
import com.finder.domain.user.dto.response.MyCommentResponse;
import com.finder.domain.user.dto.response.MyCommentsResponse;
import com.finder.domain.user.dto.response.RealFinalMyCommentsResponse;
import com.finder.domain.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getMe();
    void secession();
    List<ItemResponse> getMyItems();
    void updateUser(UserUpdateRequest request);

    RealFinalMyCommentsResponse getMyComments();
}
