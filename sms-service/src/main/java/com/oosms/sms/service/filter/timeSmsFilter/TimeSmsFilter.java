package com.oosms.sms.service.filter.timeSmsFilter;

import java.time.LocalDateTime;

public interface TimeSmsFilter {
    boolean isSendable(LocalDateTime sendDt);
}
