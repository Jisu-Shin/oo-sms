package com.oosms.sms.api;

import com.oosms.common.dto.SmsTemplateListResponseDto;
import com.oosms.common.dto.SmsTemplateRequestDto;
import com.oosms.sms.service.SmsTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name="SmsTemplateApiController", description = "sms 템플릿 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/smsTemplates")
public class SmsTemplateApiController {

    private final SmsTemplateService smsTemplateService;

    @Operation(summary="sms 템플릿 생성")
    @PostMapping
    public Long create(@RequestBody SmsTemplateRequestDto requestDto) {
        log.info("requestDto:{}", requestDto);
        return smsTemplateService.create(requestDto);
    }

    @Operation(summary="sms 템플릿 전체 조회")
    @GetMapping
    public List<SmsTemplateListResponseDto> getSmsTemplates() {
        log.info("getSmsTemplates 메소드 시작");
        return smsTemplateService.findAll();
    }
}
