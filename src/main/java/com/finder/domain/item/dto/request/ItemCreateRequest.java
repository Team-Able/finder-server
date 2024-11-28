package com.finder.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemCreateRequest(
        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotBlank
        String imageUrl,

        ItemLocationCreateRequest location
) {
}
