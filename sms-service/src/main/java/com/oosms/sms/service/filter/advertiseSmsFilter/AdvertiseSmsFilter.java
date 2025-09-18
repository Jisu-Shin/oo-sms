package com.oosms.sms.service.filter.advertiseSmsFilter;

import com.oosms.sms.domain.Sms;

public interface AdvertiseSmsFilter {
    boolean isSendable(Sms sms);
}
