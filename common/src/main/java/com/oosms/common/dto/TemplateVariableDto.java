package com.oosms.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TemplateVariableDto {

    private Long id;

    @NotNull
    private String enText;

    @NotNull
    private String koText;

    @NotNull
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
}
