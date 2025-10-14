package com.oosms.common.exception;

public class ReplacementValueNotFoundException extends BusinessException{
    public ReplacementValueNotFoundException(String variableName) {
        super("템플릿 변수(" + variableName + ")의 치환값을 찾을 수 없습니다.");
    }
}
