package com.oosms.booking.service;

import com.oosms.booking.domain.Item;
import com.oosms.booking.mapper.ItemMapper;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import com.oosms.booking.repository.JpaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final JpaItemRepository jpaItemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public Long saveItem(Item item) {
        jpaItemRepository.save(item);
        return item.getId();
    }

    public List<ItemGetResponseDto> findAll() {
        return jpaItemRepository.findAll().stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    public ItemGetResponseDto findById(Long id) {
        Item item = jpaItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 없습니다."));
        return itemMapper.toDto(item);
    }

    @Transactional
    public Long updateItem(ItemUpdateRequestDto requestDto) {
        Item findItem = jpaItemRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 없습니다."));
        findItem.update(requestDto.getName(), requestDto.getPrice(), requestDto.getStockQuantity());
        return findItem.getId();
    }
}
