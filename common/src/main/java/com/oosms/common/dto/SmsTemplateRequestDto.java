package com.oosms.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SmsTemplateRequestDto {

    private Long id;

    @NotNull @NotEmpty
    private String templateContent;

    @NotNull @NotEmpty
    private String smsType;

    @Builder
    public SmsTemplateRequestDto(Long id, String templateContent, String smsType) {
        this.id = id;
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
