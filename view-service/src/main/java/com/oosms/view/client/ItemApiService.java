package com.oosms.view.client;

import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    public List<ItemGetResponseDto> getItemAll() {
        ResponseEntity<List<ItemGetResponseDto>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ItemGetResponseDto>>() {}
        );
        return response.getBody();
    }

    public Long createItem(ItemCreateRequestDto requestDto) {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                baseUrl,
                requestDto,
                Long.class
        );
        return response.getBody();
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

    public Long updateItem(ItemUpdateRequestDto requestDto) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(String.valueOf(requestDto.getId()));
        ResponseEntity<Long> response = restTemplate.postForEntity(
                builder.toUriString(),
                requestDto,
                Long.class
        );

        return response.getBody();
    }
}
