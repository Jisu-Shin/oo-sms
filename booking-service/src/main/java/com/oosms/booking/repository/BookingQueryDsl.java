package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.repository.dto.BookingWithCustDto;

import java.util.List;

public interface BookingQueryDsl {
    public List<Booking> findAll(BookingSearch bookingSearch);
    public List<BookingWithCustDto> findBookingWithCustInfo(BookingSearch bookingSearch);
}
