package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.sms.domain.SmsTemplate;

public interface SmsTmpltVarBinder {
    public String bind(SmsTemplate smsTemplate, BindingDto bindingDto);
}
