package com.oosms.common.exception;

public class SmsTemplateNotFoundException extends BusinessException {
    public SmsTemplateNotFoundException(Long id) {
        super("해당 SMS템플릿이 없습니다 : id = " + id);
    }
}
