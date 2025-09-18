package com.oosms.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(title = "고객 조회 응답 DTO")
public class CustListResponseDto {

    @Schema(description = "아이디")
    private Long id;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "전화번호")
    private String phoneNumber;

    @Schema(description = "고객SMS수신동의타입")
    private String consentType;
}
