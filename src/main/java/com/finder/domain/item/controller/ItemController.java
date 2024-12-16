package com.finder.domain.item.controller;

import com.finder.domain.item.dto.request.ItemCreateRequest;
import com.finder.domain.item.dto.response.ItemDetailResponse;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.service.ItemService;
import com.finder.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getLostItems() {
        return BaseResponse.of(itemService.getLostItems(), 200, "분실물 목록 조회 성공");
    }

    @Operation(summary = "습득물 목록 조회")
    @GetMapping("/found")
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getFoundItems() {
        return BaseResponse.of(itemService.getFoundItems(), 200, "습득물 목록 조회 성공");
    }

    @Operation(summary = "습득")
    @PatchMapping("/{itemId}/found")
    public ResponseEntity<BaseResponse<ItemResponse>> foundItem(@PathVariable Long itemId) {
        return BaseResponse.of(itemService.foundItem(itemId), 200, "습득 성공");
    }

    @Operation(summary = "분실물 목록 조회 (최신순)")
    @GetMapping("/lost/latest")
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getLatestFoundItems() {
        return BaseResponse.of(itemService.getLatestLostItems(), 200, "습득물 목록 조회 성공");
    }

    @Operation(summary = "분실물 목록 조회 (인기순)")
    @GetMapping("/lost/popular")
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getPopularFoundItems() {
        return BaseResponse.of(itemService.getPopularLostItems(), 200, "습득물 목록 조회 성공");
    }

    @Operation(summary = "분실물 목록 조회 (가까운 순)")
    @GetMapping("/lost/region")
    public ResponseEntity<BaseResponse<List<ItemResponse>>> getRegionFoundItems(@RequestParam Double latitude, @RequestParam Double longitude) {
        return BaseResponse.of(itemService.getRegionLostItems(latitude, longitude), 200, "습득물 목록 조회 성공");
    }

    @Operation(summary = "분실물 조회")
    @GetMapping("/{itemId}")
    public ResponseEntity<BaseResponse<ItemDetailResponse>> getItem(@PathVariable Long itemId) {
        return BaseResponse.of(itemService.getItem(itemId), 200, "분실물 조회 성공");
    }

    @Operation(summary = "분실물 등록")
    @PostMapping("/lost")
    public ResponseEntity<BaseResponse<ItemResponse>> createItem(@Valid @RequestBody ItemCreateRequest request) {
        return BaseResponse.of(itemService.createItem(request), 201, "분실물 등록 성공");
    }

    @Operation(summary = "분실물 수정")
    @PatchMapping("/{itemId}")
    public ResponseEntity<BaseResponse<ItemResponse>> updateItem(@PathVariable Long itemId, @Valid @RequestBody ItemCreateRequest request) {
        return BaseResponse.of(itemService.updateItem(itemId, request), 200, "분실물 수정 성공");
    }

    @Operation(summary = "분실물 삭제")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<BaseResponse<Void>> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);

        return BaseResponse.of(null, 200, "분실물 삭제 성공");
    }
}
