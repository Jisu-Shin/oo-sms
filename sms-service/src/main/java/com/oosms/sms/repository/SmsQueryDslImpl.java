package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.dto.QSmsWithCust;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oosms.cust.domain.QCust.cust;
import static com.oosms.sms.domain.QSms.sms;

@Repository
public class SmsQueryDslImpl implements SmsQueryDsl {

    private final JPAQueryFactory queryFactory;

    public SmsQueryDslImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<SmsWithCust> findBySearch(SmsListSearchCondition searchCondition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (searchCondition.getStartDate() != null && searchCondition.getEndDate() != null) {
            builder.and(sms.sendDt.between(searchCondition.getStartDate(), searchCondition.getEndDate()));
        }

        if (searchCondition.getCustName() != null && !searchCondition.getCustName().isBlank() ) {
            builder.and(cust.name.eq(searchCondition.getCustName()));
        }

        if(searchCondition.getSmsType() != null) {
            builder.and(sms.smsTemplate.smsType.eq(searchCondition.getSmsType()));
        }

        if (searchCondition.getSmsResult() != null) {
            builder.and(sms.smsResult.eq(searchCondition.getSmsResult()));
        }

        return queryFactory
                .select(new QSmsWithCust(
                        sms,
                        cust.name
                ))
                .from(sms)
                .join(cust).on(sms.custId.eq(cust.id))
                .where(builder)
                .limit(100)
                .fetch();
    }

    @Override
    public List<Sms> findBySendDt(SmsListSearchCondition smsSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        return queryFactory
                .selectFrom(sms)
                .where(builder)
                .limit(100)
                .fetch();
    }
}
