package com.oosms.view.client;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.dto.CustSaveRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    public List<CustListResponseDto> getCustId(String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("findId")
                .queryParam("name", name);

        ResponseEntity<List<CustListResponseDto>> response = restTemplate.exchange(
                builder.build(false).toUriString(), // URL을 인코딩하지 않게 원본 유지
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustListResponseDto>>() {
                }
        );
        return response.getBody();
    }

    public List<CustListResponseDto> getCustAll() {
        UriBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        System.out.println("##### cust.getCustAll url = " + builder.toUriString());
        ResponseEntity<List<CustListResponseDto>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustListResponseDto>>() {}
        );
        return response.getBody();
    }

    public Long createCust(CustSaveRequestDto requestDto) {
        UriBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                builder.toUriString(),
                requestDto,
                Long.class
        );

        return response.getBody();
    }
}
