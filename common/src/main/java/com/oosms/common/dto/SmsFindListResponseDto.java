package com.oosms.common.dto;

import com.oosms.sms.domain.Sms;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class SmsFindListResponseDto {
    private Long smsId;
    private String custName;
    private String sendPhoneNumber;
    private String smsContent;
    private String sendDt;
    private String smsType;
    private String smsResult;

    public SmsFindListResponseDto(Sms sms) {
        this.smsId = sms.getSmsId();
//        this.custName = sms.getCust().getName();
        this.sendPhoneNumber = sms.getSendPhoneNumber();
        this.smsContent = sms.getSmsContent();
        this.sendDt = sms.getSendDt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        this.smsType = sms.getSmsTemplate().getSmsType().getDisplayName();
        this.smsResult = sms.getSmsResult().getDisplayName();
    }
}
