package com.finder.domain.item.service.impl;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemResponse> getLostItems() {
        return itemRepository.findAllByStatus(ItemStatus.LOST).stream().map(ItemResponse::of).toList();
    }

    @Override
    public List<ItemResponse> getFoundItems() {
        return itemRepository.findAllByStatus(ItemStatus.FOUND).stream().map(ItemResponse::of).toList();
    }

    @Override
    public ItemResponse getItem(Long itemId) {
        return ItemResponse.of(itemRepository.findById(itemId).orElseThrow());
    }

    @Override
    public ItemResponse createItem(ItemCreateRequest request) {
        ItemEntity item = itemRepository.save(ItemEntity.builder()
                .title(request.title())
                .content(request.content())
                .status(ItemStatus.LOST)
                .build());

        return ItemResponse.of(item);
    }

    @Override
    public ItemResponse updateItem(Long itemId, ItemCreateRequest request) {
        // TODO: Implement this method

        return null;
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
