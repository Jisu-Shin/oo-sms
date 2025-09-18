package com.oosms.sms.mapper;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.sms.domain.Sms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SmsMapper {

    @Mapping(source="smsTemplate.smsType.displayName", target = "smsType")
    SmsFindListResponseDto toDto(Sms entity);

}
