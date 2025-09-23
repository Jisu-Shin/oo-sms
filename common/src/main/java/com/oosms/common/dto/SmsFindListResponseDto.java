package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SmsFindListResponseDto {
    private Long smsId;
    // todo
//    private String custName;
    private String sendPhoneNumber;
    private String smsContent;
    private String sendDt;
    private String smsType;
    private String smsResult;
}
