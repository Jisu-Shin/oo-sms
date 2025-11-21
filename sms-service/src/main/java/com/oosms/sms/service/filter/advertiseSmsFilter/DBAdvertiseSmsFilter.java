package com.oosms.sms.service.filter.advertiseSmsFilter;

import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component @Primary
public class DBAdvertiseSmsFilter implements AdvertiseSmsFilter {

    // 리포지토리 버전2) JPA 리포지토리
    private final JpaSmsRepository jpaSmsRepository;
    private final int LIMIT_SMS = 2;

    @Override
    public boolean isSendable(Sms sms) {
        LocalDateTime startDt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        LocalDateTime endDt = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0));

        SmsListSearchCondition searchCondition = SmsListSearchCondition.builder()
                .startDate(startDt)
                .endDate(endDt)
                .custId(sms.getCustId())
                .smsType(SmsType.ADVERTISING)
                .build();

        List<SmsWithCust> smsList = jpaSmsRepository.findBySearch(searchCondition);
        log.info("\nsmsList 개수 확인 {}", smsList.size());

        // 한 고객에게 광고성 문자는 하루에 2개만 발송할 수 있다.
        if (smsList.size() >= LIMIT_SMS) return false;
        return true;
    }
}
