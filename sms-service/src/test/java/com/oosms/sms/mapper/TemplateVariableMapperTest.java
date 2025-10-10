package com.oosms.sms.mapper;

import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TemplateVariableMapperTest {

    private final TemplateVariableMapper mapper = Mappers.getMapper(TemplateVariableMapper.class);
    
    @Test
    public void toDtoTest() throws Exception {
        //given
        TemplateVariable entity = TemplateVariable.create("custName", "고객명", TemplateVariableType.CUST);
        
        //when
        TemplateVariableDto dto = mapper.toDto(entity);

        //then
        assertThat(dto.getDisplayVarType()).isEqualTo("고객");
        System.out.println("dto.getVariableType() = " + dto.getVariableType());
        System.out.println("dto.getDisplayVarType() = " + dto.getDisplayVarType());
    } 

}