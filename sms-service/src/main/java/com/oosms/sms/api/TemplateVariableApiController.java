package com.oosms.sms.api;

import com.oosms.common.dto.TemplateVariableDto;
import com.oosms.sms.service.TemplateVariableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Long> create(@RequestBody @Valid TemplateVariableDto requestDto) {
        Long createdId = tmpltVarService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    @Operation(summary="템플릿 변수 전체조회")
    @GetMapping
    public List<TemplateVariableDto> findAll() {
        return tmpltVarService.findAll();
    }

    @Operation(summary = "템플릿 변수 수정")
    @PostMapping("/edit")
    public Long update(@RequestBody @Valid TemplateVariableDto requestDto) {
        return tmpltVarService.update(requestDto);
    }

    @Operation(summary = "템플릿 변수 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tmpltVarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
