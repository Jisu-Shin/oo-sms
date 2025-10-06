package com.oosms.cust.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.cust.domain.CustSmsConsentType;
import com.oosms.cust.repository.JpaCustRepository;
import com.oosms.cust.service.CustService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustApiController.class)
class CustApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustService custService;

    @MockitoBean
    private JpaCustRepository custRepository;

    @Test
    public void 고객등록() throws Exception {
        //given
        CustSaveRequestDto requestDto = CustSaveRequestDto.builder()
                .name("홍길동")
                .phoneNumber("010-1234-5678")
                .smsConsentType(CustSmsConsentType.ALL_ALLOW.name())
                .build();
        when(custService.save(any())).thenReturn(1L);

        //when & then
        mockMvc.perform(post("/api/custs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }

}