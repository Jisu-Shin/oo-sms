package com.oosms.sms.mapper;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.sms.domain.Sms;
import com.oosms.sms.domain.SmsResult;
import com.oosms.sms.repository.dto.SmsListSearchCondition;
import com.oosms.sms.repository.dto.SmsWithCust;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


class SmsMapperTest {
    private final SmsMapper mapper = Mappers.getMapper(SmsMapper.class);

    @Test
    public void toSearchConditionTest() throws Exception {
        //given
        SmsListSearchDto searchDto = new SmsListSearchDto();
        searchDto.setStartDate("202510160000");
        searchDto.setEndDate("202510232359");
        searchDto.setSmsResult(SmsResult.SUCCESS.name());

        //when
        SmsListSearchCondition searchCondition = mapper.toSearchCondition(searchDto);

        //then
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
        System.out.println("searchCondition.getSmsResult() = " + searchCondition.getSmsResult());
        System.out.println("searchCondition.getStartDate() = " + searchCondition.getStartDate());
        System.out.println("searchCondition.getEndDate() = " + searchCondition.getEndDate());
    }

    @Test
    public void toSearchConditionTest_smsReulst_null() throws Exception {
        //given
        SmsListSearchDto searchDto = new SmsListSearchDto();
        searchDto.setStartDate("202510160000");
        searchDto.setEndDate("202510232359");
        searchDto.setSmsResult("");

        //when
        SmsListSearchCondition searchCondition = mapper.toSearchCondition(searchDto);

        //then
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
        System.out.println("searchCondition.getSmsResult() = " + searchCondition.getSmsResult());
        System.out.println("searchCondition.getStartDate() = " + searchCondition.getStartDate());
        System.out.println("searchCondition.getEndDate() = " + searchCondition.getEndDate());
    }

    @Test
    public void fromSmsWithCustToDto() throws Exception {
        //given
        Sms sms = Sms.builder()
                .sendPhoneNumber("01012345978")
                .custId(1L)
                .smsContent("문자보냈어요")
                .sendDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(15,30)))
                .build();
        sms.setSmsResult(SmsResult.SUCCESS);
        SmsWithCust smsWithCust = new SmsWithCust(sms, "홍길동");

        //when
        SmsFindListResponseDto dto = mapper.toDto(smsWithCust);

        //then
        System.out.println("dto.getSendPhoneNumber() = " + dto.getSendPhoneNumber());
        System.out.println("dto.getSendDt() = " + dto.getSendDt());
        System.out.println("dto.getSmsResult() = " + dto.getSmsResult());
        System.out.println("dto.getSmsContent() = " + dto.getSmsContent());
    }

}