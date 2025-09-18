package com.oosms.sms.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CustSmsConsentType {
    ALL_ALLOW("전체 허용"),
    ALL_DENY("전체 거부"),
    ADVERTISE_DENY("광고 거부")
    ;

    private final String displayName;

    CustSmsConsentType(String displayName) {
        this.displayName = displayName;
    }

    public static CustSmsConsentType of(String label) {
        return Arrays.stream(values())
                .filter(val -> label.equals(val.displayName))
                .findFirst()
                .orElse(null);
    }

    public String getDisplayName(){
        return displayName;
    }
}
