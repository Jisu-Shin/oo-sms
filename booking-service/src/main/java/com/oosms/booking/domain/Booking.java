package com.oosms.booking.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "booking_id")
    private Long id;

    private Long custId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; // 주문 수량

    private LocalDateTime bookDt; // 예매시간

    @Enumerated(EnumType.STRING)
    private BookingStatus status; //예매상태 [예매, 취소]

    @Builder
    private Booking(Long id, Long custId, Item item, int count, LocalDateTime bookDt, BookingStatus status) {
        this.id = id;
        this.custId = custId;
        this.item = item;
        this.count = count;
        this.bookDt = bookDt;
        this.status = status;
    }

    //==생성 메서드==//
    public static Booking createBooking(Long custId, Item item, int count) {
        Booking booking = Booking.builder()
                .custId(custId)
                .count(count)
                .status(BookingStatus.BOOK)
                .bookDt(LocalDateTime.now())
                .build();
        booking.setItem(item);

        item.removeStock(count);
        return booking;
    }

    //==연관관계 메서드==//
    private void setItem(Item item) {
        this.item = item;
    }

    //==비즈니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        this.status = BookingStatus.CANCEL;
        item.addStock(count);
    }

    //== 조회 로직==//

    /**
     * 전체 티켓 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        totalPrice = count * item.getPrice();
        return totalPrice;
    }
}
