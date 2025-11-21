package com.oosms.sms.service.filter;

import static org.assertj.core.api.Assertions.*;

import com.oosms.cust.domain.Cust;
import com.oosms.sms.config.TestConfig;
import com.oosms.sms.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(properties = "spring.profiles.active=prod")
@Import(TestConfig.class)
@Transactional
class SmsFilterImplTest {

    @Autowired
    SmsFilter smsFilter;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("정상")
    void filter() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Long custId = 1L;
        SmsTemplate smsTemplate = createTemplate("안녕하세요 문자발송요", SmsType.INFORMAITONAL);
        Sms sms = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        SmsResult smsResult = smsFilter.filter(sms, CustSmsConsentType.ALL_ALLOW);
        assertThat(smsResult).isEqualTo(SmsResult.SUCCESS);
    }

    @Test
    @DisplayName("발송시간 필터링 - 발송불가")
    void filterByTime() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Long custId = 1L;
        SmsTemplate smsTemplate = createTemplate("안녕하세요 문자발송요", SmsType.INFORMAITONAL);
        Sms sms = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        SmsResult smsResult = smsFilter.filter(sms, CustSmsConsentType.ALL_ALLOW);
        assertThat(smsResult).isEqualTo(SmsResult.NOT_SEND_TIME);
    }

    @Test
    @DisplayName("발송불가시간대 - 인증문자 : 성공")
    void sendVerification() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Long custId = 1L;
        SmsTemplate smsTemplate = createTemplate("인증번호는...", SmsType.VERIFICATION);
        Sms sms = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        SmsResult smsResult = smsFilter.filter(sms, CustSmsConsentType.ALL_ALLOW);
        assertThat(smsResult).isEqualTo(SmsResult.SUCCESS);
    }

    @Test
    @DisplayName("고객마케팅동의 필터링")
    void filterByCustConsent() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(),LocalTime.of(19,0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Long custId = 1L;
        SmsTemplate smsTemplate = createTemplate("안녕하세요 문자발송요", SmsType.INFORMAITONAL);
        Sms sms = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        SmsResult smsResult = smsFilter.filter(sms, CustSmsConsentType.ALL_DENY);

        assertThat(smsResult).isEqualTo(SmsResult.CUST_REJECT);
    }

    @Test
    @DisplayName("광고메시지 정상발송")
    void sendAdvertiseMessage() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(),LocalTime.of(19,0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Long custId = 1L;
        SmsTemplate smsTemplate = createTemplate("광고문자입니다~", SmsType.ADVERTISING);
        Sms sms1 = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(smsTemplate.getTemplateContent()+"1")
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        em.persist(sms1);

        Sms sms3 = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(smsTemplate.getTemplateContent()+"3")
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();
        SmsResult smsResult = smsFilter.filter(sms3, CustSmsConsentType.ALL_ALLOW);

        assertThat(smsResult).isEqualTo(SmsResult.SUCCESS);
    }

    @Test
    @DisplayName("광고메시지 필터링")
    void filterByAdvertise() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(),LocalTime.of(19,0));
        String sendDt = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Cust cust = Cust.builder().name("홍길동").build();
        em.persist(cust);

        Long custId = cust.getId();
        SmsTemplate smsTemplate = createTemplate("광고문자입니다~", SmsType.ADVERTISING);
        Sms sms1 = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(smsTemplate.getTemplateContent()+"1")
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        Sms sms2 = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(smsTemplate.getTemplateContent()+"2")
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();

        em.persist(sms1);
        em.persist(sms2);

        Sms sms3 = Sms.builder()
                .custId(custId)
                .smsTemplate(smsTemplate)
                .smsContent(smsTemplate.getTemplateContent()+"3")
                .sendDt(ldt)
                .sendPhoneNumber("01098765432")
                .build();
        SmsResult smsResult = smsFilter.filter(sms3, CustSmsConsentType.ALL_ALLOW);

        assertThat(smsResult).isEqualTo(SmsResult.AD_COUNT_OVER);
    }

    private SmsTemplate createTemplate(String templateContent, SmsType smsType) {
        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate(templateContent, smsType);
        em.persist(smsTemplate);
        return smsTemplate;
    }
}