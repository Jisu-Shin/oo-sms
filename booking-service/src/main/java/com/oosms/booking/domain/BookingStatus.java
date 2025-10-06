package com.oosms.booking.domain;

public enum BookingStatus {
    BOOK("예매 완료"),
    CANCEL("예매 취소");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
