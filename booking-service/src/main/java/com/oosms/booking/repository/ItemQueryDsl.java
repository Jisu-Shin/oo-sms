package com.oosms.booking.repository;

import com.oosms.booking.domain.Item;

import java.util.List;

public interface ItemQueryDsl {
    public List<Item> findBySearch(ItemSearch itemSearch);
}
