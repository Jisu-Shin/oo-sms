package com.oosms.sms.service.dto;

import com.oosms.common.dto.SmsSendRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BindingDto {
    private Long custId;
    private Long itemId;

    public static BindingDto create(Long custId, SmsSendRequestDto requestDto) {
        BindingDto bindingDto = new BindingDto(custId, requestDto.getItemId());
        return bindingDto;
    }
}
