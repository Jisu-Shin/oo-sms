package com.oosms.sms.client;

import com.oosms.common.dto.CustListResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustApiService {
    private final RestTemplate restTemplate;

    @Value("${service.cust}")
    private String custUrl;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + custUrl + "/api/custs";
    }

    public CustListResponseDto getCust(Long custId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(String.valueOf(custId));

        try {
            ResponseEntity<CustListResponseDto> response = restTemplate.getForEntity(
                    builder.toUriString(),
                    CustListResponseDto.class
            );
            return response.getBody();

        } catch (Exception e) {
            log.error("cust-service 호출 중 에러 발생", e);
            throw new IllegalStateException();
        }
    }
}
