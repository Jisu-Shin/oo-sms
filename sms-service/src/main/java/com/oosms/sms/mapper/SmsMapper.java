package com.oosms.sms.mapper;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsResult;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SmsMapper {

    @Mapping(target = "smsId", source = "sms.smsId")
    @Mapping(target = "sendPhoneNumber", source = "sms.sendPhoneNumber")
    @Mapping(target = "smsContent", source = "sms.smsContent")
    @Mapping(target = "smsResult", source = "sms.smsResult.displayName")
    @Mapping(target = "sendDt", source = "sms.sendDt", dateFormat = "yyyy/MM/dd HH:mm")
    @Mapping(target = "smsType", source = "sms.smsTemplate.smsType.displayName")
    SmsFindListResponseDto toDto(SmsWithCust entity);

    @Mapping(target = "smsResult", source="smsResult", qualifiedByName = "toSmsResult")
    @Mapping(target = "startDate", dateFormat = "yyyyMMddHHmm")
    @Mapping(target = "endDate", dateFormat = "yyyyMMddHHmm")
    SmsListSearchCondition toSearchCondition(SmsListSearchDto dto);

    @Named("toSmsResult")
    default SmsResult toSmsResult(String smsResult) {
        if (smsResult != null && !smsResult.isBlank()) return SmsResult.valueOf(smsResult);
        return null;
    }

}
