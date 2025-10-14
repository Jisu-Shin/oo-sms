package com.oosms.common.exception;

public class EmptySmsTargetException extends BusinessException{
    public EmptySmsTargetException() {
        super("SMS를 발송할 고객이 없습니다.");
    }
}
