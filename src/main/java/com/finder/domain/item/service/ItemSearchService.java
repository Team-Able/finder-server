package com.finder.domain.item.service;

import com.finder.domain.item.dto.response.ItemResponse;

import java.util.List;

public interface ItemSearchService {
     List<ItemResponse> searchItem(String keyword);
     List<String> getAutoCompleteSuggestions(String prefix);
}
