package com.oosms.sms.api;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="SmsApiController", description = "sms 관련 rest api")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsApiController {

    private final SmsService smsService;

    @Operation(summary="sms 발송")
    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody SmsSendRequestDto requestDto) {
        smsService.send(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary="sms 발송목록 조회")
    @GetMapping("/sendList")
    public List<SmsFindListResponseDto> findList(SmsListSearchDto smsListSearchDto) {
        log.info("@@@@@ SmsApiController.findSmsList smsListSearchDto = {}", smsListSearchDto.toString());
        return smsService.findSmsList(smsListSearchDto);
    }

}
