package com.oosms.sms.service;

import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsTmpltVarRel;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.SmsTemplateListResponseDto;
import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.mapper.SmsTemplateMapper;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.repository.JpaSmsTmpltVarRelRepository;
import com.oosms.sms.repository.JpaTemplateVariableRepository;
import com.oosms.sms.service.smsTemplateVarBind.TemplateVariableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmsTemplateService {

    private final SmsTemplateMapper smsTemplateMapper;
    private final JpaSmsTemplateRepository smsTmpltRepository;
    private final JpaSmsTmpltVarRelRepository smsTmpltVarRelRepository;
    private final JpaTemplateVariableRepository tmpltVarRepository;

    // 템플릿 추가
    @Transactional
    public Long create(SmsTemplateRequestDto requestDto) {
        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate(requestDto.getTemplateContent(), SmsType.valueOf(requestDto.getSmsType()));

        // sms템플릿에서 변수찾기
        List<String> koTextList = TemplateVariableUtils.extractVariabels(requestDto.getTemplateContent());
        addRelation(koTextList, smsTemplate);

        smsTmpltRepository.save(smsTemplate);
        return smsTemplate.getId();
    }

    // 템플릿 수정
    @Transactional
    public Long update(SmsTemplateRequestDto requestDto) {
        SmsTemplate smsTemplate = smsTmpltRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿은 없습니다 : " + requestDto.getId()));

        // 템플릿 속성 변경
        smsTemplate.update(requestDto.getTemplateContent(), SmsType.valueOf(requestDto.getSmsType()));

        // 템플릿 관계 초기화
        smsTemplate.clearRelList();

        // 템플릿 관계 새로 생성
        List<String> koTextList = TemplateVariableUtils.extractVariabels(requestDto.getTemplateContent());
        addRelation(koTextList, smsTemplate);

        return smsTemplate.getId();
    }

    @Transactional
    public void deleteSmsTemplate(Long id) {
        SmsTemplate smsTemplate = smsTmpltRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿은 없습니다 : " + id));

        smsTmpltVarRelRepository.deleteBySmsTmpltVarRelId_SmsTmpltId(id);
        smsTmpltRepository.deleteById(id);
    }

    public List<SmsTemplateListResponseDto> findAll() {
        return smsTmpltRepository.findAll().stream()
                .map(smsTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    private void addRelation(List<String> koTextList, SmsTemplate smsTemplate) {
        for (String koText : koTextList) {
            // 템플릿 변수 검증
            TemplateVariable tmpltVar = tmpltVarRepository.findByKoText(koText)
                    .orElseThrow(() -> new IllegalArgumentException("해당 템플릿 변수는 없습니다 : " + koText));
            smsTemplate.addRelation(tmpltVar);
        }
    }
}
