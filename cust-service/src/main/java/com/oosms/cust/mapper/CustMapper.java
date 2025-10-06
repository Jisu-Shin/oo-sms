package com.oosms.cust.mapper;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.cust.domain.Cust;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CustMapper {

    // 엔티티 -> DTO
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "formatPhoneNumber")
    @Mapping(target = "consentType", source = "smsConsentType.displayName")
    CustListResponseDto toDto(Cust entity);
    Cust toEntity(CustSaveRequestDto dto);

    @Named("formatPhoneNumber")
    default String formatPhoneNumber(String phoneNumber) {
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";
        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }
}
