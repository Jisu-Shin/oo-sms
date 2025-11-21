package com.oosms.sms.config;

import com.oosms.cust.repository.JpaCustRepository;
import com.oosms.sms.client.CustApiServiceForVar;
import com.oosms.sms.client.ItemApiServiceForVar;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@EnableJpaRepositories(basePackages = "com.oosms")
@EntityScan(basePackages = "com.oosms")
public class TestConfig {
    @Bean
    public CustApiServiceForVar custApiServiceForVar() {
        return Mockito.mock(CustApiServiceForVar.class);
    }

    @Bean
    public ItemApiServiceForVar itemApiServiceForVar() {
        return Mockito.mock(ItemApiServiceForVar.class);
    }
}
