package com.finder.domain.item.service;

import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.*;

import java.util.List;

public interface ItemService {
    List<ListItemResponse> getLostItems();

    List<ListItemResponse> getLatestLostItems();

    List<ListItemResponse> getPopularLostItems();

    List<ListItemResponse> getRegionLostItems(Double latitude, Double longitude);

    List<ListItemResponse> getFoundItems();

    ItemResponse foundItem(Long itemId);

    ItemDetailResponse getItem(Long itemId);

    ItemResponse createItem(ItemCreateRequest request);

    ItemResponse updateItem(Long itemId, ItemCreateRequest request);

    void deleteItem(Long itemId);

    ItemDetailCommentResponse getItemDetailComment(Long itemId);

    List<ItemCommentResponse> getItemComments(Long itemId);
}
