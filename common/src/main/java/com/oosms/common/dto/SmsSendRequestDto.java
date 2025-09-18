package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SmsSendRequestDto {

    List<CustInfo> custIdList;

    @NotBlank(message = "SMS 발송일시를 입력해주세요.")
    String sendDt;

    @NotNull(message = "sms 템플릿을 선택해주세요.")
    Long templateId;

    Long itemId;

    @Builder
    public SmsSendRequestDto(List<CustInfo> custIdList, String sendDt, Long templateId, Long itemId) {
        this.custIdList = custIdList;
        this.sendDt = sendDt;
        this.templateId = templateId;
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "SmsSendRequestDto{" +
                "custIdList=" + custIdList +
                ", sendDt='" + sendDt + '\'' +
                ", templateId=" + templateId +
                ", itemId=" + itemId +
                '}';
    }
}
