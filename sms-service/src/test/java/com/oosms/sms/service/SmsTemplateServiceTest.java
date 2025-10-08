package com.oosms.sms.service;

import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import com.oosms.sms.mapper.SmsTemplateMapper;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.repository.JpaSmsTmpltVarRelRepository;
import com.oosms.sms.repository.JpaTemplateVariableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void 템플릿추가_정상동작() throws Exception {
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
            Field idField = SmsTemplate.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedSmsTemplate, 1L);
            return savedSmsTemplate;
        });

        //when
        Long templateId = service.create(requestDto);

        //then
        assertThat(templateId).isEqualTo(1L);
        verify(jpaSmsTemplateRepository).save(any());
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

        SmsTemplate smsTemplate = SmsTemplate.createSmsTemplate("기존 템플릿 내용", SmsType.INFORMAITONAL);
        Field idField = SmsTemplate.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(smsTemplate, 10L);

        when(jpaSmsTemplateRepository.findById(10L)).thenReturn(Optional.of(smsTemplate));

        //when
        Long updatedSmsTemplateId = service.update(requestDto);

        // then
        assertThat(updatedSmsTemplateId).isEqualTo(10L);
    }
}
