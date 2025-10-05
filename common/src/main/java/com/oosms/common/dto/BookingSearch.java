package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingSearch {
    private String custName;
    private String bookingStatus; // 예약상태 [BOOK, CANCEL]
    private Long itemId;
}
