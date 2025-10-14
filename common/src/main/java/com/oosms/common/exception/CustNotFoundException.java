package com.oosms.common.exception;

public class CustNotFoundException extends BusinessException {
    public CustNotFoundException(Long id) {
        super("해당 고객이 없습니다 : id = " + id);
    }
}
