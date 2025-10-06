package com.oosms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.oosms")
public class OoSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OoSmsApplication.class, args);
	}

}
