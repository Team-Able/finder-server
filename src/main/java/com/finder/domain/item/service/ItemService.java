package com.finder.domain.item.service;

import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.ItemResponse;

import java.util.List;

public interface ItemService {
    List<ItemResponse> getLostItems();

    List<ItemResponse> getLatestLostItems();

    List<ItemResponse> getPopularLostItems();

    List<ItemResponse> getRegionLostItems(Double latitude, Double longitude);

    List<ItemResponse> getFoundItems();

    ItemResponse foundItem(Long itemId);

    ItemResponse getItem(Long itemId);

    ItemResponse createItem(ItemCreateRequest request);

    ItemResponse updateItem(Long itemId, ItemCreateRequest request);

    void deleteItem(Long itemId);
}
