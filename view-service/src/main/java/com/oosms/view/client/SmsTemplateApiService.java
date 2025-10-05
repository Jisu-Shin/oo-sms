package com.oosms.view.client;

import com.oosms.common.dto.SmsTemplateListResponseDto;
import com.oosms.common.dto.SmsTemplateRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsTemplateApiService {

    private final RestTemplate restTemplate;

    @Value("${service.sms}")
    private String smsUrl;
    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + smsUrl + "/api/smsTemplates";
    }


    public List<SmsTemplateListResponseDto> getSmsTemplates() {
        ResponseEntity<List<SmsTemplateListResponseDto>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SmsTemplateListResponseDto>>() {}
        );
        return response.getBody();
    }

    public Long createSmsTemplate(SmsTemplateRequestDto requestDto) {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                baseUrl,
                requestDto,
                Long.class
        );

        return response.getBody();
    }

}
