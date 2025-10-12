package com.oosms.booking.mapper;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.oosms.booking.domain.Item;
import com.oosms.booking.repository.dto.BookingWithCustDto;
import com.oosms.common.dto.BookingListResponseDto;
import com.oosms.common.dto.ItemCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper bookMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    public void 예매정보변환보기() throws Exception {
        //given
        Item item2 = new Item();
        item2.setName("뮤지컬 비틀쥬스");
        item2.setPrice(150000);
        item2.setStockQuantity(100);
        
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