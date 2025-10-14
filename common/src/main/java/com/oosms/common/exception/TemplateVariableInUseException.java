package com.oosms.common.exception;

public class TemplateVariableInUseException extends BusinessException{
    public TemplateVariableInUseException(String koText) {
        super("사용하고 있는 템플릿이 있어 템플릿 변수(" + koText + ")는 삭제 불가능합니다.");
    }
}
