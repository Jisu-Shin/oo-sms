package com.oosms.sms.mapper;

import com.oosms.common.dto.SmsTemplateListResponseDto;
import com.oosms.sms.domain.SmsTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SmsTemplateMapper {

    @Mapping(source="smsType.displayName", target = "smsType")
    SmsTemplateListResponseDto toDto(SmsTemplate entity);
}
