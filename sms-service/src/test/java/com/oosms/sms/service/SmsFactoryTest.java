package com.oosms.sms.service;

import com.oosms.common.dto.CustInfo;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.sms.domain.*;
import com.oosms.sms.service.filter.SmsFilter;
import com.oosms.sms.service.smsTemplateVarBind.SmsTmpltVarBinder;
import com.oosms.sms.service.smsTemplateVarBind.dto.BindingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * SmsFactory 단위 테스트 
 * - SmsFactory의 실제 책임에만 집중 (TC-018 ~ TC-021)
 * - 의존성 조합 및 SMS 객체 생성 로직 검증
 */
@ExtendWith(MockitoExtension.class)
class SmsFactoryTest {

    @Mock
    private SmsFilter smsFilter;

    @Mock
    private SmsTmpltVarBinder smsTmpltVarBinder;

    @InjectMocks
    private SmsFactory smsFactory;

    private SmsTemplate testSmsTemplate;
    private SmsSendRequestDto testRequestDto;

    // ==== TC-016: SMS 리스트 생성 성공 테스트 ====

    @Test
    public void createSmsList_고객리스트와_템플릿으로_SMS리스트_생성_성공() throws Exception {
        //given
        setupCommonMocks();
        
        //when
        List<Sms> result = smsFactory.createSmsList(testSmsTemplate, testRequestDto);

        //then
        assertThat(result).hasSize(2);
        verifyFirstSms(result.get(0));
        verifySecondSms(result.get(1));
    }

    // ==== TC-017: 날짜 파싱 및 SMS 객체 생성 검증 ====

    @Test
    public void createSmsList_날짜파싱_및_SMS객체_올바른_생성() throws Exception {
        //given
        setupCommonMocks();
        
        //when
        List<Sms> result = smsFactory.createSmsList(testSmsTemplate, testRequestDto);

        //then
        Sms sms = result.get(0);
        // 날짜 파싱이 올바르게 되었는지 검증
        assertThat(sms.getSendDt()).isEqualTo(LocalDateTime.of(2025, 9, 6, 14, 0));
        
        // SMS 객체가 올바른 값들로 생성되었는지 검증
        assertThat(sms.getCustId()).isEqualTo(1L);
        assertThat(sms.getSmsTemplate()).isEqualTo(testSmsTemplate);
        assertThat(sms.getSendPhoneNumber()).isEqualTo("01012345678");
    }

    // ==== TC-018: 의존성 호출 순서 검증 ====

    @Test
    public void createSmsList_의존성_호출순서_검증() throws Exception {
        //given
        setupCommonMocks();
        
        //when
        smsFactory.createSmsList(testSmsTemplate, testRequestDto);

        //then
        // SmsTmpltVarBinder.bind()가 각 고객에 대해 호출되었는지 검증
        verify(smsTmpltVarBinder, times(2)).bind(eq(testSmsTemplate), any(BindingDto.class));
        
        // SmsFilter.filter()가 각 고객에 대해 호출되었는지 검증
        verify(smsFilter, times(2)).filter(any(Sms.class), any(CustSmsConsentType.class));
    }

    // ==== TC-019: 빈 고객 리스트 처리 ====

    @Test
    public void createSmsList_빈_고객리스트_빈_결과_반환() throws Exception {
        //given
        testSmsTemplate = createSmsTemplate();
        SmsSendRequestDto emptyRequestDto = SmsSendRequestDto.builder()
                .custIdList(List.of()) // 빈 리스트
                .sendDt("202509061400")
                .itemId(100L)
                .build();
        
        //when
        List<Sms> result = smsFactory.createSmsList(testSmsTemplate, emptyRequestDto);

        //then
        assertThat(result).isEmpty();
        
        // 빈 리스트일 때는 의존성 호출되지 않음을 검증
        verify(smsTmpltVarBinder, never()).bind(any(), any());
        verify(smsFilter, never()).filter(any(), any());
    }

    // ==== Helper 메서드 ====

    private void setupCommonMocks() {
        testSmsTemplate = createSmsTemplate();
        testRequestDto = createSendRequestDto();
        
        when(smsTmpltVarBinder.bind(eq(testSmsTemplate), any(BindingDto.class)))
                .thenReturn("바인딩된 메시지 내용");
        when(smsFilter.filter(any(Sms.class), any(CustSmsConsentType.class)))
                .thenReturn(SmsResult.SUCCESS);
    }

    private void verifyFirstSms(Sms sms) {
        assertThat(sms.getCustId()).isEqualTo(1L);
        assertThat(sms.getSendPhoneNumber()).isEqualTo("01012345678");
        assertThat(sms.getSmsContent()).isEqualTo("바인딩된 메시지 내용");
        assertThat(sms.getSmsTemplate()).isEqualTo(testSmsTemplate);
        assertThat(sms.getSendDt()).isEqualTo(LocalDateTime.of(2025, 9, 6, 14, 0));
        assertThat(sms.getSmsResult()).isEqualTo(SmsResult.SUCCESS);
    }

    private void verifySecondSms(Sms sms) {
        assertThat(sms.getCustId()).isEqualTo(2L);
        assertThat(sms.getSendPhoneNumber()).isEqualTo("01087654321");
    }

    private SmsTemplate createSmsTemplate() {
        return SmsTemplate.createSmsTemplate("안녕하세요 {고객명}님", SmsType.INFORMAITONAL);
    }

    private SmsSendRequestDto createSendRequestDto() {
        return SmsSendRequestDto.builder()
                .custIdList(List.of(
                        new CustInfo(1L, "01012345678", "전체 허용"),
                        new CustInfo(2L, "01087654321", "광고 거부")
                ))
                .sendDt("202509061400")  // 2025-09-06 14:00
                .itemId(100L)
                .build();
    }
}
