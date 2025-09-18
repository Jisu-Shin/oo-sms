package com.oosms.sms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateVariable {

    @Id
    @GeneratedValue
    @Column(name = "tmplt_var_id")
    private Long id;
    private String enText;
    private String koText;

    @Enumerated(EnumType.STRING)
    private TemplateVariableType variableType;

    private TemplateVariable(String enText, String koText, TemplateVariableType type) {
        this.enText = enText;
        this.koText = koText;
        this.variableType = type;
    }

    public static TemplateVariable create(String enText, String koText, TemplateVariableType type){
        TemplateVariable templateVariable = new TemplateVariable(enText, koText, type);
        return templateVariable;
    }

    @Override
    public String toString() {
        return "TemplateVariable{" +
                "id=" + id +
                ", enText='" + enText + '\'' +
                ", koText='" + koText + '\'' +
                '}';
    }
}
