package com.oosms.booking.repository;

import com.oosms.booking.domain.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingSearch {
    private BookingStatus bookingStatus; // 예약상태 [BOOK, CANCEL]
    private Long itemId;
    private String custName;

    @Builder
    public BookingSearch(BookingStatus bookingStatus, Long itemId, String custName) {
        this.bookingStatus = bookingStatus;
        this.itemId = itemId;
        this.custName = custName;
    }
}
