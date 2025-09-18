package com.oosms.cust.service;

import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.common.dto.CustUpdateRequestDto;
import com.oosms.cust.domain.Cust;
import com.oosms.cust.domain.CustSmsConsentType;
import com.oosms.cust.mapper.CustMapper;
import com.oosms.cust.repository.JpaCustRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustServiceTest {

    private final CustMapper custMapper = Mappers.getMapper(CustMapper.class);

    @Mock
    JpaCustRepository jpaCustRepository;

    private CustService custService;

    @BeforeEach
    void setUp() {
        custService = new CustService(jpaCustRepository, custMapper);
    }

    @Test
    public void 고객저장() throws Exception {
        //given
        CustSaveRequestDto requestDto = CustSaveRequestDto.builder().name("홍길동").phoneNumber("01012345678").smsConsentType("ALL_ALLOW").build();

        Cust entity = custMapper.toEntity(requestDto);
        System.out.println("entity = " + entity);

        // save 엔티티를 받아 ID 부여 후 반환
        when(jpaCustRepository.save(any())).thenAnswer(invocation -> {
            Cust cust = invocation.getArgument(0); // save 시 전달된 객체
            // id를 강제로 넣고 싶다면 reflection 사용 가능
            Field idField = Cust.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(cust, 1L);
            return cust;
        });

        //when
        Long savedCustId = custService.save(requestDto);

        //then
        assertThat(savedCustId).isEqualTo(1L);
    }

    @Test
    public void 고객수정() throws Exception {
        //given
        CustUpdateRequestDto requestDto = CustUpdateRequestDto.builder().id(1L).phoneNumber("0109999").smsConsentType("전체 거부").build();

        Cust cust = mock(Cust.class);

        // save 엔티티를 받아 ID 부여 후 반환
        when(jpaCustRepository.findById(any())).thenReturn(Optional.of(cust));

        //when
        custService.update(1L, requestDto);

        //then
        verify(cust, times(1)).update("0109999", CustSmsConsentType.ALL_DENY);
    }

}