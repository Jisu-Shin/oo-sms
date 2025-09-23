package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.common.dto.BindingDto;
import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.client.CustApiService;
import com.oosms.sms.client.ItemApiService;
import com.oosms.sms.config.TestConfig;
import com.oosms.sms.domain.*;
import com.oosms.sms.service.SmsTemplateService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/*
 스프링을 사용해서 VariableBinder 로 작성된 빈을 불러오므로
 @SpringBootTest로 테스트 필요...
 */

@SpringBootTest
@Transactional
class SmsTmpltVarBindImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    SmsTemplateService smsTemplateService;

    @Autowired
    SmsTmpltVarBinder smsTmpltVarBinder;

    @MockitoBean
    BindingDto bindingDto;

    @MockitoBean
    ItemApiService itemApiService;

    @MockitoBean
    CustApiService custApiService;


    @Test
    public void 템플릿변수enum_출력() throws Exception {
        Arrays.stream(TemplateVariableType.values())
                .collect(Collectors.toMap(e -> e, TemplateVariableType::getClassName))
                .forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    void 원본템플릿내용반환() {
        // given
        String originalContent = "안녕하세요. 테스트 메시지입니다.";
        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate(originalContent, SmsType.INFORMAITONAL);

        // when
        String result = smsTmpltVarBinder.bind(smsTemplate, bindingDto);
        System.out.println(result);

        // then
        assertThat(result).isEqualTo(originalContent);
    }

    @Test
    public void 템플릿추출() throws Exception {
        //given
        createTmpltVar("custName", "고객명", TemplateVariableType.CUST);
        createTmpltVar("performanceName", "공연명", TemplateVariableType.ITEM);
        createTmpltVar("performanceName", "공연일시", TemplateVariableType.ITEM);
        Long smsTmpltId = createSmsTmplt("#{고객명} #{공연명} #{공연일시}", SmsType.INFORMAITONAL);

        SmsTemplate smsTemplate = em.find(SmsTemplate.class, smsTmpltId);

        //when
        List<TemplateVariable> tmpltVarList = smsTemplate.getTmpltVarRelList().stream()
                .filter(rel -> rel.getTemplateVariable().getVariableType() == TemplateVariableType.ITEM)
                .map(SmsTmpltVarRel::getTemplateVariable)
                .collect(Collectors.toList());
//                .forEach(s -> System.out.println(s));

        //then
        // 아이템 템플릿 변수 개수
        assertEquals(2, tmpltVarList.size());
    }

    @Test
    public void 템플릿변수바인딩() throws Exception {
        //given
        createTmpltVar("custName", "고객명", TemplateVariableType.CUST);
        Long smsTmpltId = createSmsTmplt("#{고객명}", SmsType.INFORMAITONAL);
        SmsTemplate smsTemplate = em.find(SmsTemplate.class, smsTmpltId);

        CustListResponseDto responseDto = new CustListResponseDto();
        responseDto.setName("홍길동");
        when(custApiService.getCust(any())).thenReturn(responseDto);

        //when
        String result = smsTmpltVarBinder.bind(smsTemplate, bindingDto);
        System.out.println(result);

        //then
        assertEquals("홍길동", result);
    }

    @Test
    public void 템플릿변수_치환값이_없는_경우() throws Exception {
        //given
        createTmpltVar("custBirth", "고객생일", TemplateVariableType.CUST);
        Long smsTmpltId = createSmsTmplt("#{고객생일} 생일 축하드립니다", SmsType.INFORMAITONAL);
        SmsTemplate template = em.find(SmsTemplate.class, smsTmpltId);

        //when
        assertThrows(IllegalStateException.class, () -> smsTmpltVarBinder.bind(template, bindingDto));

        //then
    }

    private TemplateVariable createTmpltVar(String enText, String koTet, TemplateVariableType type) {
        TemplateVariable templateVariable = TemplateVariable.create(enText, koTet, type);
        em.persist(templateVariable);
        return templateVariable;
    }

    private Long createSmsTmplt(String templateContent, SmsType smsType) {
        SmsTemplateRequestDto requestDto = new SmsTemplateRequestDto(templateContent, smsType.name());
        Long smsTmpltId = smsTemplateService.create(requestDto);

        return smsTmpltId;
    }

}