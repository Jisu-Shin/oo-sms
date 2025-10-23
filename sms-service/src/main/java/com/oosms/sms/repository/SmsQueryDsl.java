package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;

import java.util.List;

public interface SmsQueryDsl {
    public List<SmsWithCust> findBySearch(SmsListSearchCondition searchCondition);
    public List<Sms> findBySendDt(SmsListSearchCondition smsSearchDto);
}
