package com.oosms.sms.service;

import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.domain.TemplateVariableType;
import com.oosms.sms.mapper.TemplateVariableMapper;
import com.oosms.sms.repository.JpaTemplateVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemplateVariableService {

    private final TemplateVariableMapper templateVariableMapper;
    private final JpaTemplateVariableRepository jpaTemplateVariableRepository;

    @Transactional
    public Long create(TemplateVariableDto dto) {
        TemplateVariable templateVariable = TemplateVariable.create(dto.getEnText(), dto.getKoText(), TemplateVariableType.of(dto.getVariableType()));
        jpaTemplateVariableRepository.save(templateVariable);

        return templateVariable.getId();
    }

    public List<TemplateVariableDto> findAll() {
        return jpaTemplateVariableRepository.findAll().stream()
                .map(templateVariableMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(TemplateVariableDto dto) {
        validateText(dto);

        TemplateVariable templateVariable = jpaTemplateVariableRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿변수는 없습니다 : " + dto.getId() + " " + dto.getKoText()));

        templateVariable.update(dto.getEnText(), dto.getKoText(), TemplateVariableType.valueOf(dto.getVariableType()));

        return templateVariable.getId();
    }

    private void validateText(TemplateVariableDto dto) {
        if(dto.getKoText() == null ||
            dto.getKoText().isEmpty()) {
            throw new IllegalArgumentException("템플릿변수 국문명이 없습니다");
        }

        if(dto.getEnText() == null ||
                dto.getEnText().isEmpty()) {
            throw new IllegalArgumentException("템플릿변수 영문명이 없습니다");
        }
    }

    @Transactional
    public void delete(Long id) {
        TemplateVariable templateVariable = jpaTemplateVariableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿변수는 없습니다 : " + id));
        jpaTemplateVariableRepository.deleteById(id);
    }
}
