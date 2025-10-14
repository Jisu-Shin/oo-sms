package com.oosms.common.exception;

public class MissingTemplateVariableException extends BusinessException{
    public MissingTemplateVariableException(String missing) {
        super("템플릿변수의 필수값인 " + missing + "이 없습니다");
    }
}
