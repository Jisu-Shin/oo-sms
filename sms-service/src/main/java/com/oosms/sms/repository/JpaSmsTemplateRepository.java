package com.oosms.sms.repository;

import com.oosms.sms.domain.SmsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSmsTemplateRepository extends JpaRepository<SmsTemplate, Long> {
}
