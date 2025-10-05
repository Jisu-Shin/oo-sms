package com.oosms.view.controller;

import com.oosms.view.client.SmsTemplateApiService;
import com.oosms.view.client.TemplateVariableApiService;
import com.oosms.common.dto.TemplateVariableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/templateVariable")
public class TemplateVariableController {

    private final TemplateVariableApiService tmpltVarApiService;

    @PostMapping("/new")
    public String create(TemplateVariableDto requestDto) {
        tmpltVarApiService.create(requestDto);
        return "redirect:/smsTemplates/new";
    }

}
