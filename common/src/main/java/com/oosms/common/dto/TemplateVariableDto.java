package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemplateVariableDto {

    private Long id;

    @NotBlank(message = "영문명은 필수값 입니다.")
    private String enText;

    @NotBlank(message = "한글명은 필수값 입니다.")
    private String koText;

    @NotBlank(message = "변수유형은 필수값 입니다.")
    private String variableType;
    private String displayVarType;
}
