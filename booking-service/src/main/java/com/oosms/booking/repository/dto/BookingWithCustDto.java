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
    private Booking booking;
    private String custName;

    @QueryProjection
    public BookingWithCustDto(Booking booking, String custName) {
        this.booking = booking;
        this.custName = custName;
    }
}
