package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item, Long>, ItemQueryDsl {
    List<Booking> findByNameLike(String search);
}
