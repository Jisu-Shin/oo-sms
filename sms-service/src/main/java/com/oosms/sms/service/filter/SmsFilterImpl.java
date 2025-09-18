package com.oosms.sms.service.filter;

import com.oosms.sms.domain.*;
import com.oosms.sms.service.filter.advertiseSmsFilter.AdvertiseSmsFilter;
import com.oosms.sms.service.filter.customerSmsFilter.CustomerSmsFilter;
import com.oosms.sms.service.filter.timeSmsFilter.TimeSmsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SmsFilterImpl implements SmsFilter {

    private final TimeSmsFilter timeSmsFilter;
    private final AdvertiseSmsFilter advertiseSmsFilter;
    private final CustomerSmsFilter customerSmsFilter;

    @Override
    public SmsResult filter(Sms sms, CustSmsConsentType type) {
        // sms 필터링
        SmsTemplate smsTemplate = sms.getSmsTemplate();

        // 0. 인증문자는 항상 나가야하는 법...
        if (SmsType.VERIFICATION == smsTemplate.getSmsType()) {
            return SmsResult.SUCCESS;
        }

        // 1. 시간
        if (!timeSmsFilter.isSendable(sms.getSendDt())) {
            return SmsResult.NOT_SEND_TIME;
        }

        // 2. 고객동의
        if (!customerSmsFilter.isSendable(type, smsTemplate.getSmsType())) {
            return SmsResult.CUST_REJECT;
        }

        // 3. 광고 개수 제한
        if (SmsType.ADVERTISING == smsTemplate.getSmsType()) {
            if (!advertiseSmsFilter.isSendable(sms)) {
                return SmsResult.AD_COUNT_OVER;
            }
        }

        return SmsResult.SUCCESS;
    }
}
