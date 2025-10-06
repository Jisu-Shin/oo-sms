package com.oosms.booking.mapper;

import com.oosms.booking.domain.Item;
import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemGetResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toEntity(ItemCreateRequestDto dto);
    ItemGetResponseDto toDto(Item entity);
}
