package com.finder.domain.item.service.impl;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.ItemDetailResponse;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.item.service.ItemService;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final SecurityHolder securityHolder;

    @Override
    public List<ItemResponse> getLostItems() {
        return itemRepository.findAllByStatus(ItemStatus.LOST).stream().map(ItemResponse::of).toList();
    }

    @Override
    public List<ItemResponse> getFoundItems() {
        return itemRepository.findAllByStatus(ItemStatus.FOUND).stream().map(ItemResponse::of).toList();
    }

    @Override
    public List<ItemResponse> getLatestLostItems() {
        return itemRepository.findAllByStatusOrderByCreatedAtDesc(ItemStatus.LOST).stream().map(ItemResponse::of).toList();
    }

    @Override
    public List<ItemResponse> getPopularLostItems() {
        return itemRepository.findAllByStatusOrderByViewCountDesc(ItemStatus.LOST).stream().map(ItemResponse::of).toList();
    }

    @Override
    public ItemResponse foundItem(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();

        item.setStatus(ItemStatus.FOUND);

        return ItemResponse.of(itemRepository.save(item));
    }

    @Override
    public List<ItemResponse> getRegionLostItems(Double latitude, Double longitude) {
        List<ItemEntity> items = itemRepository.findAllByStatus(ItemStatus.LOST);

        items.sort(Comparator.comparingDouble(item -> calculateDistance(latitude, longitude, item.getLocation().getLatitude(), item.getLocation().getLongitude())));

        return items.stream().map(ItemResponse::of).toList();
    }

    @Override
    public ItemDetailResponse getItem(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();

        item.increaseViewCount();

        return ItemDetailResponse.of(item);
    }

    @Override
    public ItemResponse createItem(ItemCreateRequest request) {
        ItemEntity item = itemRepository.save(ItemEntity.builder()
                .title(request.title())
                .author(securityHolder.getPrincipal())
                .content(request.content())
                .imageUrl(request.imageUrl())
                .location(ItemLocation.builder()
                        .latitude(request.location().latitude())
                        .longitude(request.location().longitude())
                        .build())
                .viewCount(0L)
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

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
