package com.oosms.sms.service.filter.customerSmsFilter;

import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.SmsType;

public interface CustomerSmsFilter {
    public boolean isSendable(CustSmsConsentType custSmsConsentType, SmsType smsType);
}
