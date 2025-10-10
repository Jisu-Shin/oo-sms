package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class SmsTemplateRequestDto {

    private Long id;

    @NotEmpty(message = "템플릿내용은 필수값 입니다.")
    @NotBlank(message = "템플릿내용은 필수값 입니다.")
    private String templateContent;

    @NotEmpty(message = "sms유형은 필수값 입니다.")
    @NotBlank(message = "sms유형은 필수값 입니다.")
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
