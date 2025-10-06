package com.oosms.booking.api;

import com.oosms.common.dto.BookingCreateRequestDto;
import com.oosms.common.dto.BookingListResponseDto;
import com.oosms.booking.repository.BookingSearch;
import com.oosms.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "BookingApiController" , description = "예약 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingApiController {

    private final BookingService bookingService;

    @Operation(summary = "예약 생성")
    @PostMapping()
    public Long booking(@RequestBody BookingCreateRequestDto requestDto) {
        return bookingService.book(requestDto);
    }

    @Operation(summary = "예약 취소")
    @PostMapping("/{id}/cancel")
    public Long cancelBooking(@PathVariable("id")Long id) {
        return bookingService.cancelBooking(id);
    }

    @Operation(summary = "예약 조건 검색")
    @GetMapping("/search")
    public List<BookingListResponseDto> searchBooking(@ModelAttribute BookingSearch bookingSearch) {
        log.info("검색어: {}", bookingSearch);
        return bookingService.findBooking(bookingSearch);
    }

}
