package com.oosms.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 개별 모듈 테스트 시에만 사용
//@SpringBootApplication(scanBasePackages = {"com.oosms.sms", "com.oosms.common"})
public class SmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsServiceApplication.class, args);
	}

}
