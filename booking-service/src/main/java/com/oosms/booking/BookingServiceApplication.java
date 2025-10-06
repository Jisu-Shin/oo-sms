package com.oosms.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Root에서 통합 실행 시에는 이 클래스가 사용되지 않음
// 개별 모듈 테스트 시에만 사용
//@SpringBootApplication
public class BookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}

}
