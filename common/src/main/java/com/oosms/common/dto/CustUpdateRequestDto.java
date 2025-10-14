package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustUpdateRequestDto {

    private Long id;

    @NotBlank(message = "이름은 필수값입니다")
    private String name;

    @NotBlank(message = "전화번호는 필수값입니다")
    private String phoneNumber;

    @NotBlank(message = "고객SMS발송유형은 필수값입니다")
    private String smsConsentType;

    @Builder
    public CustUpdateRequestDto(Long id, String name, String phoneNumber, String smsConsentType) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsConsentType = smsConsentType;
    }
}
