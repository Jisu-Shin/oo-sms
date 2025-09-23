package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.dto.SmsSearchDto;

import java.util.List;

public interface SmsQueryDsl {
    public List<Sms> findBySendDt(SmsSearchDto smsSearchDto);
}
