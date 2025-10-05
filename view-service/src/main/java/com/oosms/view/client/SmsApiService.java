package com.oosms.view.client;

import com.oosms.common.dto.SmsFindListResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsApiService {

    private final RestTemplate restTemplate;

    @Value("${service.sms}")
    private String smsUrl;
    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + smsUrl + "/api/sms";
    }

    public List<SmsFindListResponseDto> getSmsList(String startDt, String endDt) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/sendList")
                .queryParam("startDt", startDt)
                .queryParam("endDt", endDt);

        try{
            ResponseEntity<List<SmsFindListResponseDto>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SmsFindListResponseDto>>() {}
            );
            return Optional.ofNullable(response.getBody())
                    .orElse(Collections.emptyList());

        } catch (Exception e) {
            // 로그 기록 + fallback
            log.error("SMS API 호출 실패: startDt={}, endDt={}", startDt, endDt, e);
            return Collections.emptyList();
        }
    }

}
