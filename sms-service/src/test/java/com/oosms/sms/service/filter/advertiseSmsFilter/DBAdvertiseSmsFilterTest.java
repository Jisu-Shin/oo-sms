package com.oosms.sms.service.filter.advertiseSmsFilter;

import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.dto.SmsWithCust;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DBAdvertiseSmsFilterTest {

    @Mock
    JpaSmsRepository jpaSmsRepository;

    @InjectMocks
    DBAdvertiseSmsFilter filter;

    private SmsWithCust testSmsWithCust;
    private Sms testSms;

    @BeforeEach
    void setUp() {
        // 테스트용 SMS 객체 생성
        testSms = createAdvertisingSms();
        testSmsWithCust = createSmsWithCust(testSms);
    }

    @Test
    public void 광고메시지_정상발송_광고이력0개() throws Exception {
        //given
        when(jpaSmsRepository.findBySearch(any())).thenReturn(Collections.emptyList());

        //when
        boolean sendable = filter.isSendable(testSms);

        //then
        assertThat(sendable).isTrue();
    }

    @Test
    public void 광고메시지_정상발송_광고이력1개() throws Exception {
        //given
        when(jpaSmsRepository.findBySearch(any())).thenReturn(List.of(testSmsWithCust));

        //when
        boolean sendable = filter.isSendable(testSms);

        //then
        assertThat(sendable).isTrue();
    }

    @Test
    public void 광고메시지_발송불가() throws Exception {
        //given
        when(jpaSmsRepository.findBySearch(any())).thenReturn(List.of(testSmsWithCust, testSmsWithCust));

        //when
        boolean sendable = filter.isSendable(testSms);

        //then
        assertThat(sendable).isFalse();
    }

    private Sms createAdvertisingSms() {
        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate("광고문자템플릿", SmsType.ADVERTISING);

        // 테스트용 SMS 객체 생성
        Sms sms = Sms.builder()
                .smsTemplate(smsTemplate)
                .custId(1L)
                .sendDt(LocalDateTime.now())
                .build();

        return sms;
    }

    private SmsWithCust createSmsWithCust(Sms sms) {
        SmsWithCust smsWithCust = new SmsWithCust(sms, "홍길동");
        return smsWithCust;
    }


}