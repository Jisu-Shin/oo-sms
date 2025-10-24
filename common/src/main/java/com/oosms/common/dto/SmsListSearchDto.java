package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class SmsListSearchDto {
    private String custName;
    private String startDate;
    private String endDate;
    private String smsResult;
}
