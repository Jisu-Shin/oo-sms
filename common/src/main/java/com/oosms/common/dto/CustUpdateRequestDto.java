package com.oosms.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustUpdateRequestDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String smsConsentType;

    @Builder
    public CustUpdateRequestDto(Long id, String name, String phoneNumber, String smsConsentType) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsConsentType = smsConsentType;
    }
}
