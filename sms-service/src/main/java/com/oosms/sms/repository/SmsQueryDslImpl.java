package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.dto.SmsSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oosms.sms.domain.QSms.sms;

@Repository
public class SmsQueryDslImpl implements SmsQueryDsl{

    private final JPAQueryFactory queryFactory;
    public SmsQueryDslImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Sms> findBySendDt(SmsSearchDto smsSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();
        if (smsSearchDto.getStartDt() != null && smsSearchDto.getEndDt() != null ) {
            builder.and(sms.sendDt.between(smsSearchDto.getStartDt(), smsSearchDto.getEndDt()));
        }

        if (smsSearchDto.getCustId() != null) {
            builder.and(sms.custId.eq(smsSearchDto.getCustId()));
        }

        if (smsSearchDto.getSmsType() != null) {
            builder.and(sms.smsTemplate.smsType.eq(smsSearchDto.getSmsType()));
        }

        return queryFactory
                .selectFrom(sms)
                .where(builder)
                .limit(100)
                .fetch();
    }
}
