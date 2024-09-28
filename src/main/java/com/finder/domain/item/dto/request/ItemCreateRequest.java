package com.finder.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ItemCreateRequest(
        @NotBlank
        String title,

        @NotBlank
        String content
) {
}
