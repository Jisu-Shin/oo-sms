package com.oosms.cust.mapper;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.cust.domain.Cust;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustMapper {

    // 엔티티 -> DTO
    @Mapping(source = "smsConsentType.displayName", target = "consentType")
    CustListResponseDto toDto(Cust entity);
    Cust toEntity(CustSaveRequestDto dto);
}
