package com.oosms.common.exception;

import com.oosms.common.dto.ErrorRestResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * - REST API의 모든 예외를 일관되게 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 - Not Found 예외 처리
     * - CustNotFoundException
     * - ItemNotFoundException
     * - BookingNotFoundException
     * - TemplateVariableNotFoundException
     * - SmsTemplateNotFoundException
     */
    @ExceptionHandler({
            CustNotFoundException.class,
            TemplateVariableNotFoundException.class,
            SmsTemplateNotFoundException.class,
            ReplacementValueNotFoundException.class
    })
    public ResponseEntity<ErrorRestResponseDto> handleNotFoundException(BusinessException e) {
        log.error("Not Found Exception: {}", e.getMessage());

        ErrorRestResponseDto response = ErrorRestResponseDto.of(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * 409 - Conflict 예외 처리
     * - DuplicateCustException
     * - DuplicateItemException
     */
    @ExceptionHandler({
            DuplicateCustException.class,
            DuplicateItemException.class
    })
    public ResponseEntity<ErrorRestResponseDto> handleConflictException(BusinessException e) {
        log.error("Conflict Exception: {}", e.getMessage());

        ErrorRestResponseDto response = ErrorRestResponseDto.of(
                e.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    /**
     * 400 - Bad Request 예외 처리
     * - EmptySmsTargetException
     * - MissingTemplateVariableException
     * - TemplateVariableInUseException (삭제 불가)
     * - NotEnoughStockException
     */
    @ExceptionHandler({
            EmptySmsTargetException.class,
            MissingTemplateVariableException.class,
            TemplateVariableInUseException.class,
            NotEnoughStockException.class
    })
    public ResponseEntity<ErrorRestResponseDto> handleBadRequestException(BusinessException e) {
        log.error("Bad Request Exception: {}", e.getMessage());

        ErrorRestResponseDto response = ErrorRestResponseDto.of(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 400 - Validation 예외 처리
     * - @Valid 검증 실패 시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRestResponseDto> handleValidationException(
            MethodArgumentNotValidException e) {
        log.error("Validation Exception: {}", e.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorRestResponseDto response = ErrorRestResponseDto.of(
                "입력값이 올바르지 않습니다.",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 500 - 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRestResponseDto> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);

        ErrorRestResponseDto response = ErrorRestResponseDto.of(
                "서버 내부 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
