package com.oosms.common.exception;

public class VariableBinderNotFoundException extends BusinessException{
    public VariableBinderNotFoundException(String className) {
        super("해당 클래스명을 가진 변수바인딩클래스가 없습니다. 클래스명 = " + className);
    }
}
