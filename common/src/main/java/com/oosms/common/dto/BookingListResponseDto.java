package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingListResponseDto {

    private Long bookId;
    private Long custId;
    private String custName;
    private String itemName;
    private int count;
    private String bookingStatus;
    private String bookDt;
    private boolean isBooking;

    @Override
    public String toString() {
        return "BookingListResponseDto{" +
                "bookId=" + bookId +
                ", custId=" + custId +
                ", custName='" + custName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", count=" + count +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", bookDt='" + bookDt + '\'' +
                ", isBooking=" + isBooking +
                '}';
    }
}
