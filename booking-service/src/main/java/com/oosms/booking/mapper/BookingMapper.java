package com.oosms.booking.mapper;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.oosms.booking.repository.dto.BookingWithCustDto;
import com.oosms.common.dto.BookingListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookId", source = "booking.id")
    @Mapping(target = "bookingStatus", source = "booking.status.displayName")
    @Mapping(target = "bookDt", source="booking.bookDt", dateFormat = "yyyy/MM/dd HH:mm")
    @Mapping(target = "count", source="booking.count")
    @Mapping(target = "itemName", source = "booking.item.name")
    @Mapping(target = "bookingFlag", source = "booking.status", qualifiedByName = "isBookingByStatus")
    BookingListResponseDto toDto(BookingWithCustDto entity);

    @Named("isBookingByStatus")
    default boolean isBookingByStatus(BookingStatus bookingStatus) {
        return BookingStatus.BOOK.equals(bookingStatus);
    }

}
