package com.oosms.booking.service;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.Item;
import com.oosms.booking.mapper.BookingMapper;
import com.oosms.common.dto.BookingCreateRequestDto;
import com.oosms.common.dto.BookingListResponseDto;
import com.oosms.booking.repository.BookingSearch;
import com.oosms.booking.repository.JpaBookingRepository;
import com.oosms.booking.repository.JpaItemRepository;
import com.oosms.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {

    private final JpaBookingRepository jpaBookingRepository;
    private final JpaItemRepository jpaItemRepository;
    private final BookingMapper bookingMapper;

    /**
     * 주문
     */
    @Transactional
    public Long book(BookingCreateRequestDto requestDto) {
        //엔티티조회
        Item item = jpaItemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("공연", requestDto.getItemId()));

        //예약 생성
        Booking booking = Booking.createBooking(requestDto.getCustId(), item, requestDto.getCount());

        //예약 저장
        jpaBookingRepository.save(booking);

        return booking.getId();
    }

    /**
     * 예약 취소
     */
    @Transactional
    public Long cancelBooking(Long bookingId) {
        //예약 엔티티 조회
        Booking booking = jpaBookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("예약", bookingId));

        // 예약 취소
        booking.cancel();
        return bookingId;
    }

    /**
     * 검색
     */
    public List<BookingListResponseDto> findBooking(BookingSearch bookingSearch) {
        return jpaBookingRepository.findBookingWithCustInfo(bookingSearch).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}
