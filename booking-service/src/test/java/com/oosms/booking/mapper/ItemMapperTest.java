package com.oosms.booking.mapper;

import com.oosms.booking.domain.Item;
import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemGetResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    public void 금액쉼표제거() throws Exception {
        //given
        ItemCreateRequestDto requestDto = new ItemCreateRequestDto();
        requestDto.setName("[뮤지컬] 비틀쥬스");
        requestDto.setPrice("100,000");
        requestDto.setStockQuantity("100");

        //when
        Item entity = itemMapper.toEntity(requestDto);

        //then
        assertThat(entity.getPrice()).isEqualTo(100000);
        System.out.println("entity.getPrice() = " + entity.getPrice());
    }

    @Test
    public void 금액쉼표추가() throws Exception {
        //given
        Item item = new Item(); // todo setter 삭제
        item.setPrice(180000);

        //when
        ItemGetResponseDto dto = itemMapper.toDto(item);

        //then
        assertThat(dto.getPrice()).isEqualTo("180,000");
        System.out.println("dto.getPrice() = " + dto.getPrice());
    }

}