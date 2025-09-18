package com.oosms.sms.repository;

import com.oosms.sms.domain.SmsType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SmsSearch {

    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private Long custId;
    private SmsType smsType;

    @Builder
    public SmsSearch(LocalDateTime startDt, LocalDateTime endDt, Long custId, SmsType smsType) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.custId = custId;
        this.smsType = smsType;
    }
}
