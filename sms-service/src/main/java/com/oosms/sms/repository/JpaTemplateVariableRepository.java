package com.oosms.sms.repository;

import com.oosms.sms.domain.TemplateVariable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTemplateVariableRepository extends JpaRepository<TemplateVariable, Long> {
    Optional<TemplateVariable> findByKoText(String koText);
}
