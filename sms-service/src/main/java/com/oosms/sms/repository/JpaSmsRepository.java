package com.oosms.sms.repository;

import com.oosms.sms.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaSmsRepository extends JpaRepository<Sms, Long>, SmsQueryDsl{
    List<Sms> findByCustId(Long custId);

    List<Sms> findAllBySendDtBetween(LocalDateTime startDt, LocalDateTime endDt);
}
