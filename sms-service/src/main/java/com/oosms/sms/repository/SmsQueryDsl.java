package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;

import java.util.List;

public interface SmsQueryDsl {
    public List<Sms> findBySendDt(SmsSearch smsSearch);
}
