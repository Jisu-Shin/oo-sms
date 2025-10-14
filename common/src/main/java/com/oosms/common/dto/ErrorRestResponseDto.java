package com.oosms.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST API 에러 응답 DTO
 * - 비즈니스 예외와 Validation 예외 모두 처리
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 필드는 JSON에서 제외
public class ErrorRestResponseDto {

    private final String message;           // 에러 메시지
    private final int status;               // HTTP 상태 코드
    private final LocalDateTime timestamp;  // 에러 발생 시간
    private final Map<String, String> errors;  // 필드별 에러 (Validation용)

    @Builder
    public ErrorRestResponseDto(String message, int status, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    // 단순 에러 응답 생성 (비즈니스 예외용)
    public static ErrorRestResponseDto of(String message, int status) {
        return ErrorRestResponseDto.builder()
                .message(message)
                .status(status)
                .build();
    }

    // Validation 에러 응답 생성
    public static ErrorRestResponseDto of(String message, int status, Map<String, String> errors) {
        return ErrorRestResponseDto.builder()
                .message(message)
                .status(status)
                .errors(errors)
                .build();
    }
}
