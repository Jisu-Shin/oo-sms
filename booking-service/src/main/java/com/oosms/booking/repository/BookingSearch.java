package com.oosms.booking.repository;

import com.oosms.booking.domain.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingSearch {
    private BookingStatus bookingStatus; // 예약상태 [BOOK, CANCEL]
    private Long itemId;
}
