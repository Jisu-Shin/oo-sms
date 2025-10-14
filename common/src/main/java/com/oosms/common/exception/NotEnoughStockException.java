package com.oosms.common.exception;

public class NotEnoughStockException extends BusinessException {
    public NotEnoughStockException() {
        super("남은 재고가 없습니다.");
    }
}
