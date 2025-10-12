package com.oosms.booking.repository.dto;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingWithCustDto {
    private Booking booking;
    private Long custId;
    private String custName;

    @QueryProjection
    public BookingWithCustDto(Booking booking, Long custId, String custName) {
        this.booking = booking;
        this.custId = custId;
        this.custName = custName;
    }
}
