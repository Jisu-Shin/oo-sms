package com.oosms.sms.client;

import com.oosms.common.dto.ItemGetResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ItemApiService {

    private final RestTemplate restTemplate;

    @Value("${service.booking}")
    private String bookingUrl;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + bookingUrl + "/api/items";
    }

    public ItemGetResponseDto getItem(Long itemId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(String.valueOf(itemId));
        ResponseEntity<ItemGetResponseDto> response = restTemplate.getForEntity(
                builder.toUriString(),
                ItemGetResponseDto.class
        );

        return response.getBody();
    }
}
