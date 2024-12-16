package com.finder.domain.item.controller;

import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.service.ItemSearchService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Item Search", description = "검색 관련")
@RestController
@RequestMapping("/items/search")
@RequiredArgsConstructor
public class ItemSearchController {
    private final ItemSearchService itemSearchService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ItemResponse>>> searchItems(@RequestParam String keyword) {
        List<ItemResponse> results = itemSearchService.searchItem(keyword);

        return BaseResponse.of(results, 200, "검색 성공");
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<BaseResponse<List<String>>> getAutoCompleteSuggestions(@RequestParam String prefix) {
        List<String> suggestions = itemSearchService.getAutoCompleteSuggestions(prefix);

        return BaseResponse.of(suggestions, 200, "자동완성 검색 성공");
    }
}
