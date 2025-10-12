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
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
                    ReflectionTestUtils.setField(entity, "id", 1L);
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

    @Test
    public void 템플릿변수수정_성공() throws Exception {
        //given
        String updateKoText = "공연일시";
        String updateEnText = "peformanceDt";

        TemplateVariableDto requestDto = TemplateVariableDto.builder()
                .id(5L)
                .koText(updateKoText)
                .enText(updateEnText)
                .variableType(TemplateVariableType.ITEM.name())
                .build();
        TemplateVariable templateVariable = TemplateVariable.create("custName","고객명", TemplateVariableType.CUST);
        when(jpaTemplateVariableRepository.findById(5L)).thenReturn(Optional.of(templateVariable));

        //when
        Long updatedId = templateVariableService.update(requestDto);

        //then
        assertThat(templateVariable.getKoText()).isEqualTo(updateKoText);
        assertThat(templateVariable.getEnText()).isEqualTo(updateEnText);
    }

    @Test
    public void 템플릿변수수정_실패() throws Exception {
        //given
        TemplateVariableDto requestDto = TemplateVariableDto.builder()
                .id(5L)
                .koText(null)
                .enText(null)
                .variableType(TemplateVariableType.ITEM.name())
                .build();

        //when & then
        assertThatThrownBy(() -> templateVariableService.update(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("템플릿변수 국문명이 없습니다");
    }
    
    @Test
    public void 템플릿변수삭제_성공() throws Exception {
        //given
        TemplateVariable templateVariable = TemplateVariable.create("en", "영어", TemplateVariableType.CUST);
        ReflectionTestUtils.setField(templateVariable, "id", 5L);
        when(jpaTemplateVariableRepository.findById(5L))
                .thenReturn(Optional.of(templateVariable));
        
        //when
        templateVariableService.delete(5L);

        //then
        verify(jpaTemplateVariableRepository).deleteById(5L);
    }

    @Test
    public void 템플릿변수삭제_실패() throws Exception {
        //given
        //when & then
        assertThatThrownBy(() -> templateVariableService.delete(5L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}