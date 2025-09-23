package com.oosms.sms.mapper;

import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.domain.TemplateVariable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemplateVariableMapper {

    @Mapping(target = "displayVarType", source = "variableType.displayName")
    TemplateVariableDto toDto(TemplateVariable entity);
}
