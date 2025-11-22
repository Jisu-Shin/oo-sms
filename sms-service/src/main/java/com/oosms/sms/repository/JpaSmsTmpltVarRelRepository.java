package com.oosms.sms.repository;

import com.oosms.sms.domain.SmsTmpltVarRel;
import com.oosms.sms.domain.SmsTmpltVarRelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaSmsTmpltVarRelRepository extends JpaRepository<SmsTmpltVarRel, SmsTmpltVarRelId> {
    List<SmsTmpltVarRel> findBySmsTmpltVarRelId_SmsTmpltId(Long smsTmpltId);
    List<SmsTmpltVarRel> findBySmsTmpltVarRelId_TmpltVarId(Long tmpltVarId);

    @Modifying
    @Query("DELETE FROM SmsTmpltVarRel r WHERE r.smsTemplate.id = :smsTmpltId")
    int deleteBySmsTmpltId(@Param("smsTmpltId") Long smsTmpltId);
}
