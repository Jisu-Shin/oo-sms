package com.oosms.sms.service;

import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SmsTemplateServiceTest {

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
    public void 매치패턴() throws Exception {
        String target = "#{고객명} #{공연명}";
        String regEx = "#\\{(.*?)\\}";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(target);

        // then
        assertThat(matcher.groupCount()).isEqualTo(1);

        // 첫 번째 매치
        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("고객명");

        // 두 번째 매치
        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("공연명");

        // 더 이상 매치되지 않음
        assertThat(matcher.find()).isFalse();
    }

    //todo 테스트코드에 transactional 없이 하는 방법...
    @Test
    @Transactional
    public void 템플릿추가() throws Exception {
        //given
        setUp();
        String templateContent = "#{고객명}님 안녕하세요 #{공연명}은 ...";
        SmsTemplateRequestDto requestDto = new SmsTemplateRequestDto(templateContent, SmsType.INFORMAITONAL.getDisplayName());

        //when
        Long tmpltId = smsTemplateService.create(requestDto);

        SmsTemplate savedTemplate = jpaSmsTemplateRepository.findById(tmpltId)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다"));

        //then
        assertThat(savedTemplate.getTmpltVarRelList()).hasSize(2);
        assertThat(savedTemplate.getSmsType()).isEqualTo(SmsType.INFORMAITONAL);
    }

    @Test
    @Transactional
    public void 템플릿생성_템플릿변수없음() throws Exception {
        //given
        setUp();
        String templateContent = "#{고객명}님 안녕하세요 #{템플릿변수없음}은 ...";
        SmsTemplateRequestDto requestDto = new SmsTemplateRequestDto(templateContent, SmsType.INFORMAITONAL.getDisplayName());

        //when
        assertThatThrownBy(()->smsTemplateService.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class);
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
        SmsTemplateRequestDto requestDto = new SmsTemplateRequestDto(templateContent, SmsType.INFORMAITONAL.getDisplayName());
        createdTemplateId = smsTemplateService.create(requestDto);
    }

    @Test
    @Transactional
    public void 템플릿수정() throws Exception {
        //given
        setUp();
        String templateContent = "정보성 템플릿 저장 #{고객명}님께 안내해드리는~";
        SmsTemplateRequestDto requestDto = new SmsTemplateRequestDto(templateContent, SmsType.INFORMAITONAL.getDisplayName());
        requestDto.setId(createdTemplateId);

        //when
        Long updateId = smsTemplateService.update(requestDto);

        //then
        SmsTemplate updatedTemplate = jpaSmsTemplateRepository.findById(updateId)
                .orElseThrow(() -> new IllegalArgumentException("템플릿을 찾을 수 없습니다"));
        assertThat(updatedTemplate.getTmpltVarRelList()).hasSize(1);
        assertThat(updatedTemplate.getTemplateContent()).isEqualTo(templateContent);
    }

}