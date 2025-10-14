package com.oosms.common.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String entityName, Long id) {
        super("해당 " + entityName + "이/가 없습니다 : id = " + id);
    }
}
