package com.oosms.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Root에서 통합 실행 시에는 이 클래스가 사용되지 않음
// common은 라이브러리 모듈이므로 보통 Application 클래스가 필요 없음
//@SpringBootApplication
public class CommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

}
