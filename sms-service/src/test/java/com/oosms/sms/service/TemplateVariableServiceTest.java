package com.oosms.sms.service;

import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import com.oosms.sms.mapper.TemplateVariableMapper;
import com.oosms.sms.repository.JpaTemplateVariableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateVariableServiceTest {

    @Mock
    TemplateVariableMapper templateVariableMapper;

    @Mock
    JpaTemplateVariableRepository jpaTemplateVariableRepository;

    @InjectMocks
    TemplateVariableService templateVariableService;

    @Test
    public void 템플릿변수성공() throws Exception {
        //given
        TemplateVariableDto dto = new TemplateVariableDto();
        dto.setKoText("고객명");
        dto.setEnText("custName");
        dto.setVariableType(TemplateVariableType.CUST.getDisplayName());

        when(jpaTemplateVariableRepository.save(any()))
                .thenAnswer(invocation -> {
                    TemplateVariable entity = invocation.getArgument(0);
                    // ID 할당
                    Field idField = TemplateVariable.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(entity, 1L);
                    return entity;
                });

        //when
        Long createdTemplateVar = templateVariableService.create(dto);

        //then
        assertThat(createdTemplateVar).isEqualTo(1L);
    }

    @Test
    public void 모든템플릿변수찾기() throws Exception {
        //given
        TemplateVariable tmpltVar1 = TemplateVariable.create("custName", "고객명", TemplateVariableType.CUST);
        TemplateVariable tmpltVar2 = TemplateVariable.create("custBirth", "고객생일", TemplateVariableType.CUST);
        List<TemplateVariable> tmpltVarList = List.of(tmpltVar1, tmpltVar2);
        when(jpaTemplateVariableRepository.findAll()).thenReturn(tmpltVarList);

        //when
        List<TemplateVariableDto> list = templateVariableService.findAll();

        //then
        assertThat(list).hasSize(2);
    }
}