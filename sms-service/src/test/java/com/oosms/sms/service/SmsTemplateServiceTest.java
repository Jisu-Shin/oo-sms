package com.oosms.sms.service;

import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.domain.*;
import com.oosms.sms.mapper.SmsTemplateMapper;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.repository.JpaSmsTmpltVarRelRepository;
import com.oosms.sms.repository.JpaTemplateVariableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsTemplateServiceTest {

    @Mock
    private SmsTemplateMapper smsTemplateMapper;

    @Mock
    private JpaSmsTemplateRepository jpaSmsTemplateRepository;

    @Mock
    private JpaSmsTmpltVarRelRepository jpaSmsTmpltVarRelRepository;

    @Mock
    private JpaTemplateVariableRepository jpaTemplateVariableRepository;

    @InjectMocks
    private SmsTemplateService service;

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

    @Test
    public void 템플릿추가_정상() throws Exception {
        //given
        String templateContent = "#{고객명}님 안녕하세요 #{공연명}은 ...";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();

        // 템플릿 변수 있음
        TemplateVariable tmpltVar1 = TemplateVariable.create("custName", "고객명", TemplateVariableType.CUST);
        TemplateVariable tmpltVar2 = TemplateVariable.create("itemName", "공연명", TemplateVariableType.ITEM);
        when(jpaTemplateVariableRepository.findByKoText("고객명")).thenReturn(Optional.of(tmpltVar1));
        when(jpaTemplateVariableRepository.findByKoText("공연명")).thenReturn(Optional.of(tmpltVar2));

        // 생성된 SmsTemplate ID 설정
        when(jpaSmsTemplateRepository.save(any())).thenAnswer(invocation -> {
            SmsTemplate savedSmsTemplate = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedSmsTemplate, "id", 1L);
            return savedSmsTemplate;
        });

        //when
        Long templateId = service.create(requestDto);

        //then
        System.out.println("templateId = " + templateId);
        assertThat(templateId).isEqualTo(1L);
        verify(jpaSmsTemplateRepository).save(any());
        verify(jpaTemplateVariableRepository, times(2)).findByKoText(any());
    }

    @Test
    public void 템플릿추가_템플릿내용null() throws Exception {
        //given
        String templateContent = null;
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();

        //when & then
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sms템플릿 내용이 없습니다");
    }

    @Test
    public void 템플릿추가_템플릿변수없음() throws Exception {
        //given
        String templateContent = "#{해당템플릿변수없음}은 ...";
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(templateContent)
                .smsType(SmsType.INFORMAITONAL.name())
                .build();

        //when & then
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 템플릿 변수는 없습니다");
    }

    @Test
    public void 템플릿수정_해당템플릿없음() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .id(10L)
                .templateContent("해당 템플릿이 없네요 ㅠㅠ")
                .build();

        //when & then
        assertThatThrownBy(() -> service.update(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 템플릿은 없습니다");
    }

    @Test
    public void 템플릿수정_정상() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .id(10L)
                .templateContent("[광고문자] 로 템플릿 수정하고 싶어요")
                .smsType(SmsType.ADVERTISING.name())
                .build();

        SmsTemplate smsTemplate = createTestSmsTemplateWithId(10L, "기존 템플릿 내용");
        when(jpaSmsTemplateRepository.findById(10L)).thenReturn(Optional.of(smsTemplate));

        //when
        Long updatedSmsTemplateId = service.update(requestDto);

        // then
        assertThat(updatedSmsTemplateId).isEqualTo(10L);
        assertThat(smsTemplate.getTemplateContent()).isEqualTo(requestDto.getTemplateContent());
        assertThat(smsTemplate.getSmsType()).isEqualTo(SmsType.ADVERTISING);

        System.out.println("smsTemplate = " + smsTemplate.getTemplateContent());
        System.out.println("smsTemplate = " + smsTemplate.getSmsType());
    }

    @Test
    public void 템플릿변수가있는수정_정상() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .id(10L)
                .templateContent("[광고문자] 로 템플릿 수정하고 싶어요 #{광고번호} ")
                .smsType(SmsType.ADVERTISING.name())
                .build();

        SmsTemplate smsTemplate = createTestSmsTemplateWithId(10L, "#{고객명} 기존 템플릿 내용");
        TemplateVariable tmpltVar1 = TemplateVariable.create("marketingNum","광고번호", TemplateVariableType.ITEM);
        TemplateVariable tmpltVar2 = TemplateVariable.create("custName","고객명", TemplateVariableType.CUST);
        when(jpaSmsTemplateRepository.findById(10L)).thenReturn(Optional.of(smsTemplate));
        when(jpaTemplateVariableRepository.findByKoText("광고번호")).thenReturn(Optional.of(tmpltVar1));

        //when
        Long updatedSmsTemplateId = service.update(requestDto);

        // then
        assertThat(updatedSmsTemplateId).isEqualTo(10L);
        assertThat(smsTemplate.getTemplateContent()).isEqualTo(requestDto.getTemplateContent());
        assertThat(smsTemplate.getSmsType()).isEqualTo(SmsType.ADVERTISING);

        System.out.println("smsTemplate = " + smsTemplate.getTemplateContent());
        System.out.println("smsTemplate = " + smsTemplate.getSmsType());
    }

    private static SmsTemplate createTestSmsTemplateWithId(long id, String templateContent) {
        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate(templateContent, SmsType.INFORMAITONAL);
        ReflectionTestUtils.setField(smsTemplate,"id", id);
        return smsTemplate;
    }

    @Test
    public void 템플릿삭제_예외발생() throws Exception {
        //given

        //when & then
        assertThatThrownBy(() -> service.deleteSmsTemplate(10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 템플릿은 없습니다");

    }

    @Test
    public void 템플릿삭제_정상() throws Exception {
        //given
        SmsTemplate smsTemplate = createTestSmsTemplateWithId(10L, "기존 템플릿 내용");
        when(jpaSmsTemplateRepository.findById(10L)).thenReturn(Optional.of(smsTemplate));

        //when
        service.deleteSmsTemplate(10L);

        //then
        verify(jpaSmsTemplateRepository).deleteById(10L);
    }
}
