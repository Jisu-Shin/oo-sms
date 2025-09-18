package com.oosms.sms.service.filter.advertiseSmsFilter;

import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.SmsSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

        SmsSearch smsSearch = SmsSearch.builder()
                .startDt(startDt)
                .endDt(endDt)
                .custId(sms.getCustId())
                .smsType(SmsType.ADVERTISING)
                .build();
        List<Sms> smsList = jpaSmsRepository.findBySendDt(smsSearch);
//        List<Sms> todaySmsList = jpaSmsRepository.findAllBySendDtBetween(startDt, endDt);

        // 한 고객에게 광고성 문자는 하루에 2개만 발송할 수 있다.
        if (smsList.size() >= LIMIT_SMS) return false;
        return true;
    }
}
