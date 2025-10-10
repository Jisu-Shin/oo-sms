package com.oosms.view.controller;

import com.oosms.view.client.CustApiService;
import com.oosms.common.dto.CustSaveErrorResponseDto;
import com.oosms.common.dto.CustSaveRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@Profile("prod")
@RequiredArgsConstructor
@RequestMapping("/custs")
public class CustController {

    private final CustApiService custApiService;

    @GetMapping("")
    public String getCustList(Model model) {
        model.addAttribute("custs", custApiService.getCustAll());
        return "cust-findAll";
    }

    @GetMapping("/new")
    public String createCust() {
        return "cust-createForm";
    }

    @PostMapping("/new")
    public String save(@Valid CustSaveRequestDto requestDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            CustSaveErrorResponseDto errorResponse = getErrorResponse(result);
            model.addAttribute("errors", errorResponse);
            model.addAttribute("requestDto", requestDto);
            return "cust-createForm";
        }

        custApiService.createCust(requestDto);
        return "redirect:/custs";
    }

    private CustSaveErrorResponseDto getErrorResponse(BindingResult result) {
        CustSaveErrorResponseDto errorResponse = CustSaveErrorResponseDto.builder().build();
        result.getFieldErrors().forEach(error -> {
            switch (error.getField()) {
                case "name" -> {
                    errorResponse.setErrorName(true);
                    errorResponse.setNameDefaultMsg(error.getDefaultMessage());
                }

                case "phoneNumber" -> {
                    errorResponse.setErrorPhoneNumber(true);
                    errorResponse.setPhoneNumberDefaultMsg(error.getDefaultMessage());
                }

                case "smsConsentType" -> {
                    errorResponse.setErrorSmsConsentType(true);
                    errorResponse.setSmsConsentTypeDefaultMsg(error.getDefaultMessage());
                }
            }
        });
        return errorResponse;
    }
}
