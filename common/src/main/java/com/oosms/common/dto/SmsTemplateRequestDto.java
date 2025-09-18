package com.oosms.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SmsTemplateRequestDto {

    private Long id;
    private String templateContent;
    private String smsType;

    @Builder
    public SmsTemplateRequestDto(String templateContent, String smsType) {
        this.templateContent = templateContent;
        this.smsType = smsType;
    }

    @Override
    public String toString() {
        return "SmsTemplateRequestDto{" +
                "id=" + id +
                ", templateContent='" + templateContent + '\'' +
                ", smsType=" + smsType +
                '}';
    }
}
