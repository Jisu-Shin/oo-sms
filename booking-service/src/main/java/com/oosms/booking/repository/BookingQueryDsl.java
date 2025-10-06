package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;

import java.util.List;

public interface BookingQueryDsl {
    public List<Booking> findAll(BookingSearch bookingSearch);
}
