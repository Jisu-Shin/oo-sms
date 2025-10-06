package com.oosms.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class ViewConfig {

    @Bean
    public RestTemplate viewRestTemplate() {
        return new RestTemplate();
    }
}
