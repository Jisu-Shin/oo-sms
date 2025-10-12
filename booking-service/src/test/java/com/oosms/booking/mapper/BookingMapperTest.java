package com.oosms.booking.mapper;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.oosms.booking.domain.Item;
import com.oosms.booking.repository.dto.BookingWithCustDto;
import com.oosms.common.dto.BookingListResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper bookMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    public void 예매정보변환보기() throws Exception {
        //given
        Item item2 = Item.builder()
                .name("뮤지컬 비틀쥬스")
                .price(150000)
                .stockQuantity(200).build();
        
        Booking booking = Booking.builder()
                .item(item2)
                .bookDt(LocalDateTime.now())
                .status(BookingStatus.BOOK)
                .build();
        BookingWithCustDto bookingWithCustDto = new BookingWithCustDto(booking, 1L, "홍길동");

        //when
        BookingListResponseDto dto = bookMapper.toDto(bookingWithCustDto);

        //then
        System.out.println("dto = " + dto);
    }

}