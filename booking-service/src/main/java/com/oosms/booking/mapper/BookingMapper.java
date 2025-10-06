package com.oosms.booking.mapper;

import com.oosms.booking.domain.Booking;
import com.oosms.common.dto.BookingListResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingListResponseDto toDto(Booking entity);

}
