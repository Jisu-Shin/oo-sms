package com.oosms.sms.repository.dto;

import com.oosms.sms.domain.Sms;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SmsWithCust {
    private Sms sms;
    private String custName;

    @QueryProjection
    public SmsWithCust(Sms sms, String custName) {
        this.sms = sms;
        this.custName = custName;
    }
}
