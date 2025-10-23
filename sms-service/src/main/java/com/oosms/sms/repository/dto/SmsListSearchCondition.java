package com.oosms.sms.repository.dto;

import com.oosms.sms.domain.SmsResult;
import com.oosms.sms.domain.SmsType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor
public class SmsListSearchCondition {
    private Long custId;
    private String custName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SmsResult smsResult;
    private SmsType smsType;

    @Builder
    public SmsListSearchCondition(Long custId, String custName, LocalDateTime startDate, LocalDateTime endDate, SmsResult smsResult, SmsType smsType) {
        this.custId = custId;
        this.custName = custName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.smsResult = smsResult;
        this.smsType = smsType;
    }
}
