package com.oosms.booking.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

//TODO 나중에 시간이 되면 넣어보는 걸로
@Entity
@Getter
public class Seat {
    @Id @GeneratedValue
    @Column(name="seat_id")
    private Long id;

    private List<String> seatNumber;

//    @ManyToOne
//    @JoinColumn(name="booking_id")
//    private Booking booking;

    @Enumerated(EnumType.STRING)
    private SeatGrade grade;

    private int seatPrice;

}
