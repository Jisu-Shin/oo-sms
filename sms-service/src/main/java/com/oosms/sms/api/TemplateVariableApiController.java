package com.oosms.sms.api;

import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.service.TemplateVariableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="TemplateVariableApiController", description = "템플릿 변수 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/templateVariables")
public class TemplateVariableApiController {

    private final TemplateVariableService tmpltVarService;

    @Operation(summary="템플릿 변수 생성")
    @PostMapping
    public Long create(@RequestBody TemplateVariableDto requestDto) {
        return tmpltVarService.create(requestDto);
    }

    @Operation(summary="템플릿 변수 전체조회")
    @GetMapping
    public List<TemplateVariableDto> findAll() {
        return tmpltVarService.findAll();
    }

}
