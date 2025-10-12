package com.oosms.booking.repository.dto;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingWithCustDto {
    private BookingStatus bookingStatus; // 예약상태 [BOOK, CANCEL]
    private Long itemId;
    private String custName;
}
