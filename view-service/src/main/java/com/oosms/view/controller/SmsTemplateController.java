package com.oosms.view.controller;


import com.oosms.common.dto.ErrorResponseDto;
import com.oosms.view.client.SmsTemplateApiService;
import com.oosms.view.client.TemplateVariableApiService;
import com.oosms.common.dto.SmsTemplateRequestDto;
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

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@Profile("prod")
@RequiredArgsConstructor
@RequestMapping("/smsTemplates")
public class SmsTemplateController {

    private final SmsTemplateApiService smsTemplateApiService;
    private final TemplateVariableApiService tmpltVarApiServie;

    @GetMapping("/new")
    public String createTemplateForm(Model model) {
        model.addAttribute("templates", smsTemplateApiService.getSmsTemplates());
        model.addAttribute("placeholders", tmpltVarApiServie.getTemplateVariables());
        return "template-create";
    }

    @PostMapping("/new")
    public String createTemplate(@Valid SmsTemplateRequestDto requestDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, ErrorResponseDto> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(),
                            new ErrorResponseDto(true, error.getDefaultMessage())));

            model.addAttribute("errors", errors);
            return "template-create";
        }

        smsTemplateApiService.createSmsTemplate(requestDto);
        return "redirect:/smsTemplates/new";
    }

}
