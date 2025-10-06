package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookingRepository extends JpaRepository<Booking, Long>, BookingQueryDsl {
}
