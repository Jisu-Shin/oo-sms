package com.oosms.sms.domain;

public enum SmsResult {
    SUCCESS("성공"),
    NOT_SEND_TIME("발송 불가 시간"),
    CUST_REJECT("고객 거절"),
    AD_COUNT_OVER("광고 개수 초과");

    private final String displayName;

    SmsResult(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
