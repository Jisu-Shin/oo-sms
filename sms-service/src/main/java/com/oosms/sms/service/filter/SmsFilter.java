package com.oosms.sms.service.filter;

import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsResult;

public interface SmsFilter {
    SmsResult filter(Sms sms, CustSmsConsentType type);
}
