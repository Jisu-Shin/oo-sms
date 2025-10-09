package com.oosms.booking.mapper;

import com.oosms.booking.domain.Item;
import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemGetResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "price", qualifiedByName = "removeCommaFromPrice")
    Item toEntity(ItemCreateRequestDto dto);

    @Mapping(target = "price", qualifiedByName = "addCommaToPrice")
    ItemGetResponseDto toDto(Item entity);

    @Named("removeCommaFromPrice")
    default int removeCommaFromPrice(String price) {
        return Integer.parseInt(price.replaceAll(",", ""));
    }

    @Named("addCommaToPrice")
    default String addCommaToPrice(int price) {
        String strPrice = String.valueOf(price);
        String result = strPrice.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return result;
    }
}
