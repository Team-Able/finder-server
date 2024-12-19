package com.finder.domain.user.dto.response;

import com.finder.domain.item.dto.response.ItemCommentResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RealFinalMyCommentsResponse(
        List<FinalMyCommentsResponse> comments
) {
    public static RealFinalMyCommentsResponse convertToCustomFormat(MyCommentsResponse myCommentsResponse) {
        List<FinalMyCommentsResponse> groupedComments = myCommentsResponse.myComments().stream()
                .collect(Collectors.groupingBy(MyCommentResponse::itemId))
                .entrySet().stream()
                .map(entry -> new FinalMyCommentsResponse(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
        return new RealFinalMyCommentsResponse(groupedComments);
    }
}
