package com.oosms.sms.service;

import com.oosms.common.dto.SmsTemplateListResponseDto;
import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.common.exception.SmsTemplateNotFoundException;
import com.oosms.common.exception.TemplateVariableNotFoundException;
import com.oosms.sms.config.TestConfig;
import com.oosms.sms.domain.*;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
@Transactional //todo 테스트코드에 transactional 없이 하는 방법...
class SmsTemplateServiceIntegrationTest {

    @Autowired
    SmsTemplateService smsTemplateService;

    @Autowired
    JpaSmsTemplateRepository jpaSmsTemplateRepository;

    @Autowired
    EntityManager em;

    private static Long createdTemplateId;

    void setUp() {
        templateVariableSetUp();
        templateSetUp();
    }

    @Test
    public void 템플릿추가() throws Exception {
        //given
        setUp();
        String templateContent = "#{고객명}님 안녕하세요 #{공연명}은 ...";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();

        //when
        Long tmpltId = smsTemplateService.create(requestDto);

        SmsTemplate savedTemplate = jpaSmsTemplateRepository.findById(tmpltId)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다"));

        //then
        assertThat(savedTemplate.getTmpltVarRelList()).hasSize(2);
        assertThat(savedTemplate.getSmsType()).isEqualTo(SmsType.INFORMAITONAL);
    }

    @Test
    public void 템플릿생성_템플릿변수없음() throws Exception {
        //given
        setUp();
        String templateContent = "#{고객명}님 안녕하세요 #{템플릿변수없음}은 ...";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();

        //when
        assertThatThrownBy(()->smsTemplateService.create(requestDto))
                .isInstanceOf(TemplateVariableNotFoundException.class);
    }

    @Test
    public void 템플릿수정() throws Exception {
        //given
        setUp();
        String templateContent = "정보성 템플릿 저장 #{고객명}님께 안내해드리는~";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();
        requestDto.setId(createdTemplateId);

        //when
        Long updateId = smsTemplateService.update(requestDto);

        //then
        SmsTemplate updatedTemplate = jpaSmsTemplateRepository.findById(updateId)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다"));
        assertThat(updatedTemplate.getTmpltVarRelList()).hasSize(1);
        assertThat(updatedTemplate.getTemplateContent()).isEqualTo(templateContent);
    }

    @Test
    public void 템플릿수정_템플릿없음_Exception() throws Exception {
        //given
        String templateContent = "없는 템플릿 입니다";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();
        requestDto.setId(2L);

        //when
        assertThatThrownBy(() -> smsTemplateService.update(requestDto)).isInstanceOf(SmsTemplateNotFoundException.class);
    }

    @Test
    public void 템플릿모두조회() throws Exception {
        //given
        createTemplateVariable("custName", "고객명", TemplateVariableType.CUST);
        createTemplateVariable("performanceName", "공연명", TemplateVariableType.ITEM);
        templateSetUp();
        templateSetUp();

        //when
        List<SmsTemplateListResponseDto> all = smsTemplateService.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    public void 템플릿삭제() throws Exception {
        //given
        templateVariableSetUp();
        templateSetUp();

        //when
        smsTemplateService.deleteSmsTemplate(createdTemplateId);

        //then
        // 템플릿 삭제 확인
        assertThat(jpaSmsTemplateRepository.findById(createdTemplateId)).isEmpty();

        // 관계 삭제 확인
        List<SmsTmpltVarRel> rels = em.createQuery(
                        "SELECT r FROM SmsTmpltVarRel r WHERE r.smsTemplate.id = :tmpltId",
                        SmsTmpltVarRel.class)
                .setParameter("tmpltId", createdTemplateId)
                .getResultList();
        assertThat(rels).isEmpty();

        // 변수는 남아 있는지 확인
        List<TemplateVariable> vars = em.createQuery("SELECT v FROM TemplateVariable v", TemplateVariable.class)
                .getResultList();
        assertThat(vars)
                .extracting("koText")
                .containsExactlyInAnyOrder("고객명", "공연명");
    }

    private void templateVariableSetUp() {
        createTemplateVariable("custName", "고객명", TemplateVariableType.CUST);
        createTemplateVariable("performanceName", "공연명", TemplateVariableType.ITEM);
    }

    private void createTemplateVariable(String enText, String koText, TemplateVariableType templateVariableType) {
        TemplateVariable tmpltVar = TemplateVariable.create(enText, koText, templateVariableType);
        em.persist(tmpltVar);
        em.flush(); // DB insert 쿼리 넣기
    }

    private void templateSetUp() {
        String templateContent = "정보성 템플릿 저장 #{고객명}님 안녕하세요 #{공연명}은 ...";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();
        createdTemplateId = smsTemplateService.create(requestDto);
    }

}