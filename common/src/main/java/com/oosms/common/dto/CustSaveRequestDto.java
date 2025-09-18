package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CustSaveRequestDto {

    @NotBlank(message = "이름은 필수값입니다")
    private String name;
    @NotBlank(message = "전화번호는 필수값입니다")
    private String phoneNumber;
    @NotNull(message = "고객SMS발송유형은 필수값입니다")
    private String smsConsentType;

    @Builder
    public CustSaveRequestDto(String name, String phoneNumber, String smsConsentType) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsConsentType = smsConsentType;
    }

    @Override
    public String toString() {
        return "CustSaveRequestDto{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", smsConsentType=" + smsConsentType +
                '}';
    }
}
