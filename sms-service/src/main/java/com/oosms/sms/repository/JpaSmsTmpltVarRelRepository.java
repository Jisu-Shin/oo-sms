package com.oosms.sms.repository;

import com.oosms.sms.domain.SmsTmpltVarRel;
import com.oosms.sms.domain.SmsTmpltVarRelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSmsTmpltVarRelRepository extends JpaRepository<SmsTmpltVarRel, SmsTmpltVarRelId> {
    List<SmsTmpltVarRel> findBySmsTmpltVarRelId_SmsTmpltId(Long smsTmpltId);
}
