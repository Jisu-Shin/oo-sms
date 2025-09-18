package com.oosms.cust.repository;

import com.oosms.cust.domain.Cust;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface JpaCustRepository extends JpaRepository<Cust, Long> {
    List<Cust> findAll();
    Optional<Cust> findByName(String name);
    List<Cust> findByNameLike(String name);
    List<Cust> findByIdIn(List<Long> idList);
}
