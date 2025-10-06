package com.oosms.booking.mapper;

import com.oosms.booking.domain.Booking;
import com.oosms.common.dto.BookingListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookId", source = "id")
    @Mapping(target = "itemName", source = "item.name")
    @Mapping(target = "bookingStatus", source = "status")
    BookingListResponseDto toDto(Booking entity);

}
