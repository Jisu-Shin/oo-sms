package com.oosms.sms.api;

import com.oosms.common.dto.SmsFindListResponseDto;
import com.oosms.common.dto.SmsSendRequestDto;
import com.oosms.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="SmsApiController", description = "sms 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsApiController {

    private final SmsService smsService;

    @Operation(summary="sms 발송")
    @PostMapping("/send")
    public boolean send(@Valid @RequestBody SmsSendRequestDto requestDto) {
        return smsService.send(requestDto);
    }

    @Operation(summary="sms 발송목록 조회")
    @GetMapping("/sendList")
    public List<SmsFindListResponseDto> findList(@RequestParam("startDt") String startDt
            , @RequestParam("endDt") String endDt) {
        return smsService.findSmsList(startDt, endDt);
    }

}
