package com.oosms.booking.repository;

import com.oosms.booking.domain.Item;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oosms.booking.domain.QItem.item;

@Repository
public class ItemQueryDslImpl implements ItemQueryDsl {
    private final JPAQueryFactory queryFactory;

    public ItemQueryDslImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<Item> findBySearch(ItemSearch itemSearch) {
        BooleanBuilder builder = new BooleanBuilder();
        if (itemSearch.getName() != null) {
            builder.and(item.name.eq(itemSearch.getName()));
        }

        builder.and(item.price.eq(itemSearch.getPrice()));

        return queryFactory
                .selectFrom(item)
                .where(builder)
                .limit(100)
                .fetch();
    }
}
