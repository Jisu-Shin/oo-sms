package com.oosms.common.exception;

public class TemplateVariableNotFoundException extends BusinessException {
    public TemplateVariableNotFoundException(String koText) {
        super("해당 템플릿변수가 없습니다 : 한글명 = " + koText);
    }

    public TemplateVariableNotFoundException(Long id) {
        super("해당 템플릿변수가 없습니다 : id = " + id);
    }
}
