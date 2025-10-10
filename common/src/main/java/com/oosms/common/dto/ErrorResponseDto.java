package com.oosms.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ErrorResponseDto {
    private boolean hasError;
    private String message;
}
