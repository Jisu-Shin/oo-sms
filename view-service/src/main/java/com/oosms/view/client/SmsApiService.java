package com.oosms.view.client;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
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

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsApiService {
    //todo view/client 필요한것인지.. 삭제해도 되는게 아닌지 검토 필요
    private final RestTemplate restTemplate;

    @Value("${service.sms}")
    private String smsUrl;
    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + smsUrl + "/api/sms";
    }

    public List<SmsFindListResponseDto> getSmsList(SmsListSearchDto smsListSearchDto) {
        log.info("SmsApiService.getSmsList smsListSearchDto = {}", smsListSearchDto.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/sendList")
                .queryParam("startDate", smsListSearchDto.getStartDate())
                .queryParam("endDate", smsListSearchDto.getEndDate());

        if (smsListSearchDto.getCustName() != null) {
            builder.queryParam("custName", smsListSearchDto.getCustName());
        }

        if (smsListSearchDto.getSmsResult() != null) {
            builder.queryParam("smsResult", smsListSearchDto.getSmsResult());
        }

        URI uri = builder.encode().build().toUri();

        try{
            ResponseEntity<List<SmsFindListResponseDto>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SmsFindListResponseDto>>() {}
            );
            return Optional.ofNullable(response.getBody())
                    .orElse(Collections.emptyList());

        } catch (Exception e) {
            // 로그 기록 + fallback
            log.error("SMS API 호출 실패: startDt={}, endDt={}", smsListSearchDto.getStartDate(), smsListSearchDto.getEndDate(), e);
            return Collections.emptyList();
        }
    }

}
