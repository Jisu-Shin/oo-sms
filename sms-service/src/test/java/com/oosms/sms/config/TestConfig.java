package com.oosms.sms.config;

import com.oosms.sms.client.CustApiServiceForVar;
import com.oosms.sms.client.ItemApiServiceForVar;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

//    @Bean
//    public RestTemplate restTemplate(){
//        return Mockito.mock(RestTemplate.class);
//    }

    @Bean
    public CustApiServiceForVar custApiService() {
        return Mockito.mock(CustApiServiceForVar.class);
    }

    @Bean
    public ItemApiServiceForVar itemApiService() {
        return Mockito.mock(ItemApiServiceForVar.class);
    }
}
