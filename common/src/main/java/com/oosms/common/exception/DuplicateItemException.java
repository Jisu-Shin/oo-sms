package com.oosms.common.exception;

public class DuplicateItemException extends BusinessException {
    public DuplicateItemException(String name) {
        super("이미 존재하는 공연입니다 : 공연명 = " + name);
    }
}
