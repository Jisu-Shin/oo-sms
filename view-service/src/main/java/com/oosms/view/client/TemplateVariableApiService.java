package com.oosms.view.client;

import com.oosms.common.dto.TemplateVariableDto;
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
public class TemplateVariableApiService {

    private final RestTemplate restTemplate;

    @Value("${service.sms}")
    private String smsUrl;

    private String baseUrl;
    @PostConstruct
    private void init() {
        baseUrl = "http://" + smsUrl + "/api/templateVariables";
    }

    public Long create(TemplateVariableDto requestDto) {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                baseUrl,
                requestDto,
                Long.class
        );

        return response.getBody();
    }

    public List<TemplateVariableDto> getTemplateVariables() {
        ResponseEntity<List<TemplateVariableDto>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TemplateVariableDto>>() {}
        );
        return response.getBody();
    }
}
