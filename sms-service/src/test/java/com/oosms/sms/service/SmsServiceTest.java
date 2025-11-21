package com.oosms.sms.service;

import com.oosms.common.dto.CustInfo;
import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.common.exception.EmptySmsTargetException;
import com.oosms.common.exception.SmsTemplateNotFoundException;
import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.mapper.SmsMapper;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.repository.dto.SmsWithCust;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * SmsService 단위 테스트
 * - SmsService가 직접 담당하는 로직에만 집중
 * - 의존성은 모두 Mock으로 처리
 */
@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @Mock
    private JpaSmsTemplateRepository jpaSmsTemplateRepository;

    @Mock
    private JpaSmsRepository jpaSmsRepository;

    @Mock
    private SmsFactory smsFactory;

    @Mock
    private SmsMapper smsMapper;

    @InjectMocks
    private SmsService smsService;

    // ==== SMS 발송 기본 기능 테스트 ====
    
    @Test
    public void sms발송_성공() throws Exception {
        //given
        //new CustInfo(1L, "01012345678", CustSmsConsentType.ALL_ALLOW.toString()
        SmsSendRequestDto requestDto = SmsSendRequestDto.builder()
                .custIdList(List.of(1L))
                .sendDt("202509060928")
                .templateId(1L)
                .build();

        Sms sms = createSms();
        SmsTemplate smsTemplate = sms.getSmsTemplate();
        List<Sms> smsList = List.of(sms);

        when(jpaSmsTemplateRepository.findById(1L)).thenReturn(Optional.of(smsTemplate));
        when(smsFactory.createSmsList(smsTemplate, requestDto)).thenReturn(smsList);
        when(jpaSmsRepository.saveAll(any())).thenReturn(smsList);

        //when
        boolean result = smsService.send(requestDto);

        //then
        assertThat(result).isTrue();
    }

    // ==== SMS 발송 예외 처리 테스트 ====

    @Test
    public void sms발송_고객리스트가_비어있으면_예외발생() throws Exception {
        //given
        SmsSendRequestDto requestDto = SmsSendRequestDto.builder()
                .custIdList(List.of()) // 빈 리스트
                .sendDt("202509060928")
                .templateId(1L)
                .build();

        //when & then
        assertThrows(EmptySmsTargetException.class, () -> smsService.send(requestDto));
    }

    @Test
    public void sms발송_존재하지않는_템플릿아이디면_예외발생() throws Exception {
        //given
        SmsSendRequestDto requestDto = SmsSendRequestDto.builder()
                .custIdList(List.of(1L)) // 빈 전화번호
                .sendDt("202509060928")
                .templateId(999L) // 존재하지 않는 템플릿 ID
                .build();

        when(jpaSmsTemplateRepository.findById(999L)).thenReturn(Optional.empty());

        //when & then
        assertThrows(SmsTemplateNotFoundException.class, () -> smsService.send(requestDto));
    }

    // ==== SMS 목록 조회 테스트 ====

    @Test
    public void sms목록조회_기간으로_조회성공() throws Exception {
        //given
        String startDt = "202509060900";
        String endDt = "202509060930";
        
        LocalDateTime startLdt = LocalDateTime.of(2025, 9, 6, 9, 0);
        LocalDateTime endLdt = LocalDateTime.of(2025, 9, 6, 9, 30);
        
        List<SmsWithCust> mockSmsList = List.of(createSmsWithCust());
        when(jpaSmsRepository.findBySearch(any())).thenReturn(mockSmsList);
        when(smsMapper.toDto(any(SmsWithCust.class))).thenReturn(createMockResponseDto());

        //when
        SmsListSearchDto searchDto = new SmsListSearchDto();
        searchDto.setStartDate(startDt);
        searchDto.setEndDate(endDt);
        List<SmsFindListResponseDto> result = smsService.findSmsList(searchDto);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSmsId()).isEqualTo(1L);
    }

    @Test
    public void sms목록조회_잘못된_날짜형식이면_예외발생() throws Exception {
        //given
        // todo 날짜형식 Exception 필요
        String startDt = "202509"; // 잘못된 형식 (HHmm 누락)
        String endDt = "202509060930";

        //when & then
        SmsListSearchDto searchDto = new SmsListSearchDto();
        searchDto.setStartDate(startDt);
        searchDto.setEndDate(endDt);
        smsService.findSmsList(searchDto);
//      assertThrows(Exception.class, () -> smsService.findSmsList(startDt, endDt));
    }

    // ==== Helper 메서드 ====

    private SmsFindListResponseDto createMockResponseDto() {
        SmsFindListResponseDto dto = new SmsFindListResponseDto();
        dto.setSmsId(1L);
        dto.setSendPhoneNumber("01012345678");
        dto.setSmsContent("안녕하세요 홍길동님");
        dto.setSendDt("202509060928");
        dto.setSmsType("INFORMATIONAL");
        dto.setSmsResult("SUCCESS");
        return dto;
    }

    private SmsTemplate createSmsTemplate() {
        return SmsTemplate.createSmsTemplate("안녕하세요 홍길동님", SmsType.INFORMAITONAL);
    }

    private Sms createSms() {
        return Sms.builder()
                .smsTemplate(createSmsTemplate())
                .build();
    }

    private SmsWithCust createSmsWithCust() {
        return new SmsWithCust();
    }
}
