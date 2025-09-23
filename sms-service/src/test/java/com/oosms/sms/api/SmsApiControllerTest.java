package com.oosms.sms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oosms.common.dto.CustInfo;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.repository.JpaSmsRepository;
import com.oosms.sms.repository.JpaSmsTemplateRepository;
import com.oosms.sms.service.SmsFactory;
import com.oosms.sms.service.SmsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SmsApiController.class)
class SmsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SmsService smsService;

    @MockitoBean
    private JpaSmsTemplateRepository jpaSmsTemplateRepository;

    @MockitoBean
    private JpaSmsRepository jpaSmsRepository;

    @MockitoBean
    private SmsFactory smsFactory;

    @Test
    @DisplayName("sms 정상발송")
    public void sendSms() throws Exception {
        //given
        SmsSendRequestDto requestDto = SmsSendRequestDto.builder()
                .custIdList(List.of(new CustInfo(1L, "01012345678", CustSmsConsentType.ALL_ALLOW.toString())))
                .sendDt("202509060928")
                .templateId(1L)
                .build();
        when(smsService.send(any())).thenReturn(true);

        //when
        mockMvc.perform(post("/api/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        //then
    }
}