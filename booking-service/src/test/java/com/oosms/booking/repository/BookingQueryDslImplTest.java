package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.domain.BookingStatus;
import com.oosms.booking.domain.Item;
import com.oosms.booking.repository.dto.BookingWithCustDto;
import com.oosms.cust.domain.Cust;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "com.oosms")
class BookingQueryDslImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookingQueryDslImpl bookingQueryDsl;

    private Cust cust1, cust2;
    private Item item1, item2;
    private Booking booking1, booking2, booking3;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        cust1 = Cust.builder().name("홍길동").build();
        cust2 = Cust.builder().name("김철수").build();
        entityManager.persist(cust1);
        entityManager.persist(cust2);

        item1 = Item.builder()
                .name("뮤지컬 캣츠")
                .price(50000)
                .stockQuantity(100).build();

        item2 = Item.builder()
                .name("뮤지컬 비틀쥬스")
                .price(150000)
                .stockQuantity(200).build();

        entityManager.persist(item1);
        entityManager.persist(item2);

        booking1 = Booking.builder()
                .custId(cust1.getId())
                .item(item1)
                .count(5)
                .bookDt(LocalDateTime.now())
                .status(BookingStatus.BOOK)
                .build();

        booking2 = Booking.builder()
                .custId(cust1.getId())
                .item(item2)
                .count(7)
                .bookDt(LocalDateTime.now())
                .status(BookingStatus.CANCEL)
                .build();

        booking3 = Booking.builder()
                .custId(cust2.getId())
                .item(item2)
                .count(11)
                .bookDt(LocalDateTime.now())
                .status(BookingStatus.BOOK)
                .build();

        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("조건없음")
    public void findBookingWithCustInfo_조건없음() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
//                .bookingStatus()
//                .itemId()
//                .custName()
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        assertThat(resultList).hasSize(3);
    }

    @Test
    @DisplayName("예매완료된것만 조회")
    public void findBookingWithCustInfo_예매완료() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
                .bookingStatus(BookingStatus.BOOK)
//                .itemId()
//                .custName()
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        assertThat(resultList).hasSize(2);
    }

    @Test
    @DisplayName("예매취소된것만 조회")
    public void findBookingWithCustInfo_예매취소() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
                .bookingStatus(BookingStatus.CANCEL)
//                .itemId()
//                .custName()
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        String itemName = resultList.get(0).getBooking().getItem().getName();
        assertThat(resultList).hasSize(1);
        assertThat(itemName).isEqualTo(item2.getName());
    }

    @Test
    @DisplayName("고객명으로 조회")
    public void findBookingWithCustInfo_고객명으로조회() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
//                .bookingStatus(BookingStatus.CANCEL)
//                .itemId()
                .custName(cust1.getName())
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        String custName = resultList.get(0).getCustName();
        assertThat(resultList).hasSize(2);
        assertThat(custName).isEqualTo(cust1.getName());
    }

    @Test
    @DisplayName("상품ID로 조회")
    public void findBookingWithCustInfo_상품ID로조회() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
//                .bookingStatus(BookingStatus.CANCEL)
                .itemId(item2.getId())
//                .custName(cust1.getName())
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        String itemName = resultList.get(0).getBooking().getItem().getName();
        assertThat(resultList).hasSize(2);
        assertThat(itemName).isEqualTo(item2.getName());
    }

    @Test
    @DisplayName("복합 조건 조회")
    public void findBookingWithCustInfo_복합조건조회() throws Exception {
        //given
        BookingSearch bookingSearch = BookingSearch.builder()
                .bookingStatus(BookingStatus.CANCEL)
                .itemId(item2.getId())
//                .custName(cust1.getName())
                .build();

        //when
        List<BookingWithCustDto> resultList = bookingQueryDsl.findBookingWithCustInfo(bookingSearch);

        //then
        assertThat(resultList).hasSize(1);
    }
}