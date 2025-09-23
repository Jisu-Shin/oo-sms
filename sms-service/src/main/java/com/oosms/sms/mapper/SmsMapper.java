package com.oosms.sms.mapper;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.sms.domain.Sms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SmsMapper {

    @Mapping(target = "smsType", source="smsTemplate.smsType.displayName")
    @Mapping(target = "sendDt", dateFormat = "yyyy/MM/dd HH:mm")
    SmsFindListResponseDto toDto(Sms entity);

}
