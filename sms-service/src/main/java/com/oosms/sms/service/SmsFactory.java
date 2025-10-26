package com.oosms.sms.service;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.cust.service.CustService;
import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsResult;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.sms.service.filter.SmsFilter;
import com.oosms.sms.service.dto.BindingDto;
import com.oosms.sms.service.smsTemplateVarBind.SmsTmpltVarBinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsFactory {
    private final SmsFilter smsFilter;
    private final SmsTmpltVarBinder smsTmpltVarBinder;
    private final CustService custService;

    public List<Sms> createSmsList(SmsTemplate smsTemplate, SmsSendRequestDto requestDto) {
        return requestDto.getCustIdList().stream()
                .map(custInfo -> create(custInfo, smsTemplate, requestDto))
                .collect(Collectors.toList());
    }

    private Sms create(Long custId, SmsTemplate smsTemplate, SmsSendRequestDto requestDto) {
        // 1. 문자내용 바인딩
        String bindSmsContent = smsTmpltVarBinder.bind(smsTemplate, BindingDto.create(custId, requestDto));
        log.info("@@@@@ 생성된 문자내용 {}" , bindSmsContent);

        // 2. cust dto 가져오기
        CustListResponseDto cust = custService.findById(custId);
        log.info("@@@@@ cust dto 조회완료 {}" , cust);

        // 3. 문자 엔티티 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime sendDt = LocalDateTime.parse(requestDto.getSendDt(), formatter);

        Sms sms = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(bindSmsContent)
                .sendDt(sendDt)
                .sendPhoneNumber(cust.getPhoneNumber())
                .build();
        log.info("@@@@@ sms 생성완료");

        // 4. 필터링
        SmsResult smsResult = smsFilter.filter(sms, CustSmsConsentType.of(cust.getConsentType()));
        log.info("@@@@@ sms 필터완료 {}", smsResult);

        // 5. 필터링 결과 세팅
        sms.setSmsResult(smsResult);
        log.info("@@@@@ sms 필터완료 결과 세팅 완료");

        return sms;
    }
}
