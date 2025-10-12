package com.oosms.view.client;

import com.oosms.common.dto.BookingCreateRequestDto;
import com.oosms.common.dto.BookingListResponseDto;
import com.oosms.common.dto.BookingSearch;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingApiService {
    public final RestTemplate restTemplate;
    public final CustApiService custApiService;

    @Value("${service.booking}")
    private String bookingUrl;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://" + bookingUrl + "/api/bookings";
    }

    public Long cancelBooking(Long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(String.valueOf(id), "cancel");

        ResponseEntity<Long> response = restTemplate.postForEntity(
                builder.toUriString(),
                id,
                Long.class
        );
        return response.getBody();
    }

    public List<BookingListResponseDto> searchBooking(BookingSearch bookingSearch) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("search");

        if (bookingSearch.getBookingStatus() != null) {
            builder.queryParam("bookingStatus", bookingSearch.getBookingStatus());
        }

        if (bookingSearch.getItemId() != null) {
            builder.queryParam("itemId", bookingSearch.getItemId());
        }

        // 1. booking 검색
        ResponseEntity<List<BookingListResponseDto>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingListResponseDto>>() {
                }
        );

        return response.getBody();
    }

    public Long book(BookingCreateRequestDto requestDto) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                builder.toUriString(),
                requestDto,
                Long.class
        );

        return response.getBody();
    }

}
