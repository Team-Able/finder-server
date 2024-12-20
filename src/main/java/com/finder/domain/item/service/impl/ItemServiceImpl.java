package com.finder.domain.item.service.impl;

import com.finder.domain.item.domain.entity.ItemCommentEntity;
import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.entity.ItemLocation;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.*;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.item.service.ItemService;
import com.finder.global.security.holder.SecurityHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final SecurityHolder securityHolder;

    @Override
    public List<ListItemResponse> getLostItems() {
        return itemRepository.findAllByStatus(ItemStatus.LOST).stream().map(ListItemResponse::of).toList();
    }

    @Override
    public List<ListItemResponse> getFoundItems() {
        return itemRepository.findAllByStatus(ItemStatus.FOUND).stream().map(ListItemResponse::of).toList();
    }

    @Override
    public List<ListItemResponse> getLatestLostItems() {
        return itemRepository.findAllByStatusOrderByCreatedAtDesc(ItemStatus.LOST).stream().map(ListItemResponse::of).toList();
    }

    @Override
    public List<ListItemResponse> getPopularLostItems() {
        return itemRepository.findAllByStatusOrderByViewCountDesc(ItemStatus.LOST).stream().map(ListItemResponse::of).toList();
    }

    @Override
    public ItemResponse foundItem(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();

        item.setStatus(ItemStatus.FOUND);

        return ItemResponse.of(itemRepository.save(item));
    }

    @Override
    public List<ListItemResponse> getRegionLostItems(Double latitude, Double longitude) {
        List<ItemEntity> items = itemRepository.findAllByStatus(ItemStatus.LOST);

        items.sort(Comparator.comparingDouble(item -> calculateDistance(latitude, longitude, item.getLocation().getLatitude(), item.getLocation().getLongitude())));

        return items.stream().map(ListItemResponse::of).toList();
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

    @Override
    public ItemDetailCommentResponse getItemDetailComment(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();
        if (item.getAuthor().equals(securityHolder.getPrincipal())) {
            return ItemDetailCommentResponse.of(item);
        } else {
            return ItemDetailCommentResponse.of(item);
        }
    }

    @Override
    public List<ItemCommentResponse> getItemComments(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();
        List<ItemCommentEntity> allComments = item.getComments();
        UUID currentUserId = securityHolder.getPrincipal().getId();
        boolean isItemAuthor = item.getAuthor().equals(securityHolder.getPrincipal());

        return allComments.stream()
                .filter(comment -> comment.getParent() == null) // Get only parent comments
                .map(comment -> convertToResponse(comment, currentUserId, isItemAuthor))
                .toList();
    }

    private ItemCommentResponse convertToResponse(ItemCommentEntity comment, UUID currentUserId, boolean isItemAuthor) {
        // 댓글 내용이 보이는 조건:
        // 1. 게시글 작성자이거나
        // 2. 자신이 작성한 댓글인 경우
        boolean isCommentVisible = isItemAuthor ||
                comment.getAuthor().getId().equals(currentUserId);

        String content = isCommentVisible ? comment.getContent() : "비밀 댓글입니다.";

        List<ItemCommentResponse> children = Optional.ofNullable(comment.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .map(child -> {
                    // 대댓글 내용이 보이는 조건:
                    // 1. 게시글 작성자이거나
                    // 2. 부모 댓글의 작성자가 현재 사용자인 경우 (자신의 댓글의 모든 대댓글)
                    // 3. 대댓글 작성자가 현재 사용자인 경우
                    boolean isChildVisible = isItemAuthor ||
                            comment.getAuthor().getId().equals(currentUserId) ||
                            child.getAuthor().getId().equals(currentUserId);

                    String childContent = isChildVisible ? child.getContent() : "비밀 댓글입니다.";

                    return new ItemCommentResponse(
                            child.getId(),
                            childContent,
                            ItemCommentAuthorResponse.of(child.getAuthor()),
                            Collections.emptyList(),
                            child.getCreatedAt(),
                            child.getUpdatedAt()
                    );
                })
                .toList();

        return new ItemCommentResponse(
                comment.getId(),
                content,
                ItemCommentAuthorResponse.of(comment.getAuthor()),
                children,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
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
