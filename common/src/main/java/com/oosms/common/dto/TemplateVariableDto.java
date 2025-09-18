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
    private TemplateVariableType variableType;
    private String displayVarType;

    public TemplateVariableDto(TemplateVariable entity) {
        this.enText = entity.getEnText();
        this.koText = entity.getKoText();
        this.variableType = entity.getVariableType();
        this.displayVarType = entity.getVariableType().getDisplayName();
    }
}
