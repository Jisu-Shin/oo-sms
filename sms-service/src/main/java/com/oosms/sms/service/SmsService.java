package com.oosms.sms.service;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.common.exception.EmptySmsTargetException;
import com.oosms.common.exception.SmsTemplateNotFoundException;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.mapper.SmsMapper;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsService {
    private final JpaSmsTemplateRepository jpaSmsTemplateRepository;
    private final JpaSmsRepository jpaSmsRepository;
    private final SmsFactory smsFactory;
    private final SmsMapper smsMapper;

    @Transactional
    public boolean send(SmsSendRequestDto requestDto) {
        log.info("@@@@@ SmsService.send requestDto {}", requestDto.toString());

        validateRequest(requestDto);

        SmsTemplate smsTemplate = getSmsTemplate(requestDto.getTemplateId());

        List<Sms> smsList = smsFactory.createSmsList(smsTemplate, requestDto);
        log.info("@@@@@ smsFactory 완료");

        jpaSmsRepository.saveAll(smsList);
        log.info("@@@@@ repository saveAll 완료");

        return true;
    }

    private void validateRequest(SmsSendRequestDto requestDto) {
        if(requestDto.getCustIdList().isEmpty()) {
            throw new EmptySmsTargetException();
        }
    }

    /**
     * sms 발송 목록을 조회하는 서비스
     *
     */
    @Transactional
    public List<SmsFindListResponseDto> findSmsList(SmsListSearchDto smsListSearchDto) {
        log.info("@@@@@ SmsService.findSmsList = {}" , smsListSearchDto.toString());
        SmsListSearchCondition searchCondition = smsMapper.toSearchCondition(smsListSearchDto);
        return jpaSmsRepository.findBySearch(searchCondition).stream()
                .map(smsMapper::toDto)
                .collect(Collectors.toList());
    }

    private SmsTemplate getSmsTemplate(Long templateId) {
        return jpaSmsTemplateRepository.findById(templateId)
                .orElseThrow(() -> new SmsTemplateNotFoundException(templateId));
    }
}
