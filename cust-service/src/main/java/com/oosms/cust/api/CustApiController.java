package com.oosms.cust.api;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.common.dto.CustUpdateRequestDto;
import com.oosms.cust.service.CustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CustApiController", description = "고객 관련 rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custs")
public class CustApiController {

    private final CustService custService;

    @Operation(summary = "고객 전체 조회")
    @GetMapping("")
    public List<CustListResponseDto> findAllCust() {
        List<CustListResponseDto> list = custService.findAll();
        return list;
    }

    @Operation(summary = "고객 등록")
    @PostMapping("")
    public ResponseEntity<Long> save(@RequestBody @Valid CustSaveRequestDto requestDto) {
        String cleanPhoneNum = requestDto.getPhoneNumber().replaceAll("-","");
        requestDto.setPhoneNumber(cleanPhoneNum);
        Long custId = custService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(custId);
    }

    @Operation(summary = "고객 단건 조회")
    @GetMapping("/{id}")
    public CustListResponseDto findById(@PathVariable("id") Long id) {
        return custService.findById(id);
    }

    @Operation(summary = "고객 단건 수정")
    @PutMapping("/{id}")
    public Long update(@PathVariable("id") Long id, @RequestBody CustUpdateRequestDto requestDto) {
        return custService.update(id, requestDto);
    }

    @Operation(summary = "이름으로 고객 찾기")
    @GetMapping("/findId")
    public List<CustListResponseDto> findByName(@RequestParam("name") String name) {
        System.out.println("apiController name = " + name);
        return custService.findByName(name);
    }

    @Operation(summary = "idList로 고객 다건 조회")
    @PostMapping("/findByIdList")
    public List<CustListResponseDto> findByIdList(@RequestBody List<Long> custIdList) {
        return custService.findByIdList(custIdList);
    }

}
