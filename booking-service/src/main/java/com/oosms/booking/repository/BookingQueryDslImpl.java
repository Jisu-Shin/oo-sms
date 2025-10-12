package com.oosms.booking.repository;

import com.oosms.booking.domain.Booking;
import com.oosms.booking.repository.dto.BookingWithCustDto;
import com.oosms.booking.repository.dto.QBookingWithCustDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oosms.booking.domain.QBooking.booking;
import static com.oosms.cust.domain.QCust.cust;

@Repository
public class BookingQueryDslImpl implements BookingQueryDsl {

    private final JPAQueryFactory queryFactory;
    public BookingQueryDslImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Booking> findAll(BookingSearch bookingSearch) {

        BooleanBuilder builder = new BooleanBuilder();
        if (bookingSearch.getBookingStatus() != null) {
            builder.and(booking.status.eq(bookingSearch.getBookingStatus()));
        }

        if (bookingSearch.getItemId() != null) {
            builder.and(booking.item.id.eq(bookingSearch.getItemId()));
        }

        return queryFactory
                .selectFrom(booking)
                .where(builder)
                .limit(100)
                .fetch();
    }

    @Override
    public List<BookingWithCustDto> findBookingWithCustInfo(BookingSearch bookingSearch) {
        BooleanBuilder builder = new BooleanBuilder();
        
        if (bookingSearch.getBookingStatus() != null) {
            builder.and(booking.status.eq(bookingSearch.getBookingStatus()));
        }

        if (bookingSearch.getItemId() != null) {
            builder.and(booking.item.id.eq(bookingSearch.getItemId()));
        }

        if (bookingSearch.getCustName() != null) {
            builder.and(cust.name.eq(bookingSearch.getCustName()));
        }

        return queryFactory
                .select(new QBookingWithCustDto(
                        booking,
                        cust.id,
                        cust.name
                ))
                .from(booking)
                .join(cust).on(booking.custId.eq(cust.id))
                .where(builder)
                .limit(100)
                .fetch();
    }
}
