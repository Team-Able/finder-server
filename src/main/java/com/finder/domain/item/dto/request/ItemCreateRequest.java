package com.finder.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ItemCreateRequest(
        @NotBlank
        String title,

        @NotBlank
        Long authorId,

        @NotBlank
        String content,

        @NotBlank
        String imageUrl,

        ItemLocationCreateRequest location
) {
}
