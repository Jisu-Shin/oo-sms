package com.oosms.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CustSaveErrorResponseDto {
    private boolean isErrorName;
    private boolean isErrorPhoneNumber;
    private boolean isErrorSmsConsentType;

    private String nameDefaultMsg;
    private String phoneNumberDefaultMsg;
    private String smsConsentTypeDefaultMsg;

    @Builder
    public CustSaveErrorResponseDto(boolean isErrorName, boolean isErrorPhoneNumber, boolean isErrorSmsConsentType, String nameDefaultMsg, String phoneNumberDefaultMsg, String smsConsentTypeDefaultMsg) {
        this.isErrorName = isErrorName;
        this.isErrorPhoneNumber = isErrorPhoneNumber;
        this.isErrorSmsConsentType = isErrorSmsConsentType;
        this.nameDefaultMsg = nameDefaultMsg;
        this.phoneNumberDefaultMsg = phoneNumberDefaultMsg;
        this.smsConsentTypeDefaultMsg = smsConsentTypeDefaultMsg;
    }
}
