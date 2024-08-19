package com.finder.domain.item.controller;

import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Item", description = "분실물 관련 API")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "분실물 목록 조회")
    @GetMapping("/lost")
    public List<ItemResponse> getLostItems() {
        return itemService.getLostItems();
    }

    @Operation(summary = "습득물 목록 조회")
    @GetMapping("/found")
    public List<ItemResponse> getFoundItems() {
        return itemService.getFoundItems();
    }

    @Operation(summary = "분실물 조회")
    @GetMapping("/{itemId}")
    public ItemResponse getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @Operation(summary = "분실물 등록")
    @PostMapping("/lost")
    @PreAuthorize("hasRole('USER')")
    public ItemResponse createItem(@RequestBody ItemCreateRequest request) {
        return itemService.createItem(request);
    }

    @Operation(summary = "분실물 수정")
    @PatchMapping("/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ItemResponse updateItem(@PathVariable Long itemId, @RequestBody ItemCreateRequest request) {
        return itemService.updateItem(itemId, request);
    }

    @Operation(summary = "분실물 삭제")
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}
