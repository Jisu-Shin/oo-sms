package com.oosms.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TemplateVariableDto {
    @NotNull
    private String enText;

    @NotNull
    private String koText;

    @NotNull
    private String variableType;
    private String displayVarType;
}
