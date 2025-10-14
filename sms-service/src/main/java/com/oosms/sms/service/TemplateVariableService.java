package com.oosms.sms.service;

import com.oosms.common.exception.MissingTemplateVariableException;
import com.oosms.common.exception.TemplateVariableInUseException;
import com.oosms.common.exception.TemplateVariableNotFoundException;
import com.oosms.sms.domain.SmsTmpltVarRel;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.domain.TemplateVariableType;
import com.oosms.sms.mapper.TemplateVariableMapper;
import com.oosms.sms.repository.JpaSmsTmpltVarRelRepository;
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
    private final JpaSmsTmpltVarRelRepository jpaSmsTmpltVarRelRepository;

    @Transactional
    public Long create(TemplateVariableDto dto) {
        TemplateVariable templateVariable = TemplateVariable.create(dto.getEnText(), dto.getKoText(), TemplateVariableType.of(dto.getDisplayVarType()));
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
                .orElseThrow(() -> new TemplateVariableNotFoundException(dto.getKoText()));

        templateVariable.update(dto.getEnText(), dto.getKoText(), TemplateVariableType.of(dto.getDisplayVarType()));

        return templateVariable.getId();
    }

    private void validateText(TemplateVariableDto dto) {
        if(dto.getKoText() == null ||
            dto.getKoText().isEmpty()) {
            throw new MissingTemplateVariableException("한글명");
        }

        if(dto.getEnText() == null ||
                dto.getEnText().isEmpty()) {
            throw new MissingTemplateVariableException("영문명");
        }
    }

    @Transactional
    public void delete(Long id) {
        TemplateVariable templateVariable = jpaTemplateVariableRepository.findById(id)
                .orElseThrow(() -> new TemplateVariableNotFoundException(id));

        List<SmsTmpltVarRel> usingSmsTmpltList = jpaSmsTmpltVarRelRepository.findBySmsTmpltVarRelId_TmpltVarId(id);
        if (usingSmsTmpltList.size() > 0) {
            throw new TemplateVariableInUseException(templateVariable.getKoText());
        }

        jpaTemplateVariableRepository.deleteById(id);
    }
}
