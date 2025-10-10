package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
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

    @Builder
    public TemplateVariableDto(Long id, String enText, String koText, String variableType, String displayVarType) {
        this.id = id;
        this.enText = enText;
        this.koText = koText;
        this.variableType = variableType;
        this.displayVarType = displayVarType;
    }

    @Override
    public String toString() {
        return "TemplateVariableDto{" +
                "id=" + id +
                ", enText='" + enText + '\'' +
                ", koText='" + koText + '\'' +
                ", variableType='" + variableType + '\'' +
                ", displayVarType='" + displayVarType + '\'' +
                '}';
    }
}
