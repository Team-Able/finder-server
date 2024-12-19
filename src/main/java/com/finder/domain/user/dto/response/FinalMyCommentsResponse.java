package com.finder.domain.user.dto.response;

import java.util.List;

public record FinalMyCommentsResponse(
        Long itemId,
        List<MyCommentResponse> comments
) {
}
