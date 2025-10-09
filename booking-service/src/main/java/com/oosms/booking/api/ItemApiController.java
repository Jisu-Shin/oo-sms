package com.oosms.booking.api;

import com.oosms.booking.mapper.ItemMapper;
import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import com.oosms.booking.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ItemApiController", description = "예약 항목 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemApiController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Operation(summary = "예약 항목 전체 조회")
    @GetMapping()
    public List<ItemGetResponseDto> findAll() {
        return itemService.findAll();
    }

    @Operation(summary = "예약 항목 생성")
    @PostMapping()
    public Long create(@RequestBody @Valid ItemCreateRequestDto requestDto) {
        return itemService.saveItem(itemMapper.toEntity(requestDto));
    }

    @Operation(summary = "예약 항목 단건 조회")
    @GetMapping("/{id}")
    public ItemGetResponseDto getItem(@PathVariable("id")Long id){
        return itemService.findById(id);
    }

    @Operation(summary = "예약 항목 수정")
    @PostMapping("/{id}")
    public Long update(@PathVariable("id")Long id, ItemUpdateRequestDto requestDto) {
        return itemService.updateItem(requestDto);
    }

}
