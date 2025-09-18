package com.oosms.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustInfo {
    Long custId;
    String phoneNumber;
    String custSmsConsentType;

    @Override
    public String toString() {
        return "CustInfo{" +
                "custId=" + custId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", custSmsConsentType='" + custSmsConsentType + '\'' +
                '}';
    }
}
