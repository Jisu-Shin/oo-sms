package com.oosms.common.exception;

public class DuplicateCustException extends BusinessException {
    public DuplicateCustException(String name) {
        super("이미 존재하는 고객입니다 : 고객명 = " + name);
    }
}
