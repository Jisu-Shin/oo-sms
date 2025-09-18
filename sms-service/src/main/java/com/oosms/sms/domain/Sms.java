package com.oosms.sms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sms extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smsId;

    private Long custId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sms_tmplt_id")
    private SmsTemplate smsTemplate;

    private String sendPhoneNumber;
    private String smsContent;
    private LocalDateTime sendDt;

    @Enumerated(EnumType.STRING)
    private SmsResult smsResult;


    @Builder
    private Sms(Long custId, SmsTemplate smsTemplate, String smsContent, LocalDateTime sendDt, String sendPhoneNumber) {
        this.custId = custId;
        this.smsTemplate = smsTemplate;
        this.smsContent = smsContent;
        this.sendPhoneNumber = sendPhoneNumber;
        this.sendDt = sendDt;
    }

    //== setter==
    public void setSmsResult(SmsResult smsResult) {
        this.smsResult = smsResult;
    }
}
