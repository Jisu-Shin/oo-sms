package com.oosms.sms.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TemplateVariableType {
    CUST("고객", "custVariableBindImpl")
    ,ITEM("공연", "itemVariableBindImpl")
    ;

    private final String displayName;
    private final String className;

    TemplateVariableType(String displayName, String className){
        this.displayName = displayName;
        this.className = className;
    }

    public static TemplateVariableType of(String displayName) {
        return Arrays.stream(values())
                .filter(val -> displayName.equals(val.displayName))
                .findFirst()
                .orElse(null);
    }
}
