package com.oosms.sms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsType;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.service.SmsTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SmsTemplateApiController.class)
class SmsTemplateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SmsTemplateService service;

    @MockitoBean
    private JpaSmsTemplateRepository jpaSmsTemplateRepository;


    @Test
    public void sms템플릿생성_content비어있음_실패() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .smsType(SmsType.ADVERTISING.getDisplayName())
                .build();

        //when & then
        mockMvc.perform(post("/api/smsTemplates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sms템플릿생성_content가null_실패() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .templateContent(null)
                .smsType(SmsType.ADVERTISING.getDisplayName())
                .build();

        //when & then
        mockMvc.perform(post("/api/smsTemplates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sms템플릿수정_성공() throws Exception {
        //given
        SmsTemplateRequestDto requestDto = SmsTemplateRequestDto.builder()
                .id(5L)
                .templateContent("[광고성] 문자 변경이요")
                .smsType(SmsType.ADVERTISING.getDisplayName())
                .build();
        when(service.update(any())).thenReturn(5L);

        //when & then
        mockMvc.perform(post("/api/smsTemplates/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void sms템플릿삭제_성공() throws Exception {
        //given
        // void 메서드는 when 설정 불필요 (Mock은 기본적으로 아무것도 안 함)

        //when & then
        mockMvc.perform(delete("/api/smsTemplates/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service, times(1)).deleteSmsTemplate(5L);
    }

    @Test
    public void sms템플릿삭제_실패_존재하지않음() throws Exception {
        //given
        doThrow(new IllegalArgumentException("해당 템플릿은 없습니다"))
                .when(service).deleteSmsTemplate(999L);

        //when & then
        mockMvc.perform(delete("/api/smsTemplates/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());  // 예외 처리가 없으면 500 에러
    }

}