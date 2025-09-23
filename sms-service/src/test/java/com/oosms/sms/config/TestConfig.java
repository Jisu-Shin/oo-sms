package com.oosms.sms.config;

import com.oosms.sms.client.CustApiService;
import com.oosms.sms.client.ItemApiService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {

//    @Bean
//    public RestTemplate restTemplate(){
//        return Mockito.mock(RestTemplate.class);
//    }

    @Bean
    public CustApiService custApiService() {
        return Mockito.mock(CustApiService.class);
    }

    @Bean
    public ItemApiService itemApiService() {
        return Mockito.mock(ItemApiService.class);
    }
}
