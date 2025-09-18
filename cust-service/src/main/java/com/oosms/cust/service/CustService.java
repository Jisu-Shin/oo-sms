package com.oosms.cust.service;

import com.oosms.common.dto.*;
import com.oosms.cust.domain.Cust;
import com.oosms.cust.domain.CustSmsConsentType;
import com.oosms.cust.mapper.CustMapper;
import com.oosms.cust.repository.JpaCustRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CustService {

    private final JpaCustRepository custRepository;
    private final CustMapper custMapper;

    /**
     * 회원 가입
     */
    @Transactional
    public Long save(CustSaveRequestDto requestDto) {
        // 중복 회원 검증
        validateDuplicateCust(requestDto);
        return custRepository.save(custMapper.toEntity(requestDto)).getId();
    }

    private void validateDuplicateCust(CustSaveRequestDto requestDto) {
        // EXCEPTION
        Optional<Cust> findCust = custRepository.findByName(requestDto.getName());
        if (!findCust.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 고객입니다.");
        }
    }

    @Transactional
    public Long update(Long id, CustUpdateRequestDto requestDto) {
        Cust cust = custRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. id = " + id));
        cust.update(requestDto.getPhoneNumber(), CustSmsConsentType.of(requestDto.getSmsConsentType()));
        return id;
    }

    public CustListResponseDto findById(Long id) {
        Cust entity = custRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 고객이 없습니다. id = " + id));

        return custMapper.toDto(entity);
    }

    /**
     * 회원 전체 조회
     */
    public List<CustListResponseDto> findAll() {
        return custRepository.findAll().stream()
                .map(custMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CustListResponseDto> findByName(String name) {
        System.out.println("name = " + name);
        return custRepository.findByNameLike("%"+name+"%").stream()
                .map(custMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CustListResponseDto> findByIdList(List<Long> custIdList) {
        return custRepository.findByIdIn(custIdList).stream()
                .map(custMapper::toDto)
                .collect(Collectors.toList());
    }
}
