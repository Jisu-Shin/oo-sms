package com.oosms.sms.repository;

import com.oosms.cust.domain.Cust;
import com.oosms.cust.domain.CustSmsConsentType;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsResult;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.oosms")
class SmsQueryDslImplTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsQueryDslImpl smsQueryDsl;

    private SmsTemplate smsTemplate1, smsTemplate2;
    private Sms sms1, sms2, sms3;
    private Cust cust1,cust2;

    @BeforeEach
    void setUp() {
        cust1 = Cust.builder()
                .name("홍길동")
                .phoneNumber("01012345678")
                .smsConsentType(CustSmsConsentType.ALL_ALLOW)
                .build();
        cust2 = Cust.builder()
                .name("김철수")
                .phoneNumber("01098765432")
                .smsConsentType(CustSmsConsentType.ALL_ALLOW)
                .build();
        entityManager.persist(cust1);
        entityManager.persist(cust2);

        smsTemplate1 = SmsTemplate.createSmsTemplate("기본문자 보냅니다용", SmsType.INFORMAITONAL);
        smsTemplate2 = SmsTemplate.createSmsTemplate("광고문자 보냅니다용", SmsType.ADVERTISING);
        entityManager.persist(smsTemplate1);
        entityManager.persist(smsTemplate2);

        sms1 = Sms.builder()
                .custId(cust1.getId())
                .smsTemplate(smsTemplate1)
                .smsContent("기본문자 보냅니다용")
                .sendDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(15,30)))
                .build();
        sms1.setSmsResult(SmsResult.SUCCESS);

        sms2 = Sms.builder()
                .custId(cust1.getId())
                .smsTemplate(smsTemplate2)
                .smsContent("광고문자 보냅니다용")
                .sendDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16,30)))
                .build();
        sms2.setSmsResult(SmsResult.CUST_REJECT);

        sms3 = Sms.builder()
                .custId(cust2.getId())
                .smsTemplate(smsTemplate1)
                .smsContent("기본문자 보냅니다용")
                .sendDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(17,30)))
                .build();
        sms3.setSmsResult(SmsResult.SUCCESS);

        entityManager.persist(sms1);
        entityManager.persist(sms2);
        entityManager.persist(sms3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findBySearch_조건없음() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(3);
    }

    @Test
    public void findBySearch_날짜조건_시작값경계() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setStartDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(00,00)));
        searchCondition.setEndDate(LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(23,59)));

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(3);
    }

    @Test
    public void findBySearch_날짜조건_종료값경계() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setStartDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(00,00)));
        searchCondition.setEndDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59)));

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(3);
    }

    @Test
    public void findBySearch_날짜조건_결과없음() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setStartDate(LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(00,00)));
        searchCondition.setEndDate(LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(23,59)));

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(0);
    }

    @Test
    public void findBySearch_이름조건() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setCustName("홍길동");

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(2);
    }

    @Test
    public void findBySearch_발송결과조건() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setSmsResult(SmsResult.CUST_REJECT);

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(1);
    }

    @Test
    public void findBySearch_smstype() throws Exception {
        //given
        SmsListSearchCondition searchCondition = new SmsListSearchCondition();
        searchCondition.setSmsType(SmsType.ADVERTISING);

        //when
        List<SmsWithCust> resultList = smsQueryDsl.findBySearch(searchCondition);

        //then
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0).getSms().getSmsTemplate().getTemplateContent()).isEqualTo(smsTemplate2.getTemplateContent());
    }

}