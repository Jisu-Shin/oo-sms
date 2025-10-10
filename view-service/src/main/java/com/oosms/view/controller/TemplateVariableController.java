package com.oosms.view.controller;

import com.oosms.common.dto.ErrorResponseDto;
import com.oosms.view.client.SmsTemplateApiService;
import com.oosms.view.client.TemplateVariableApiService;
import com.oosms.common.dto.TemplateVariableDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/templateVariable")
public class TemplateVariableController {

    private final TemplateVariableApiService tmpltVarApiService;

    @PostMapping("/new")
    public String create(@Valid TemplateVariableDto requestDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, ErrorResponseDto> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(),
                            new ErrorResponseDto(true, error.getDefaultMessage())));

            model.addAttribute("errors", errors);
            model.addAttribute("requestDto", requestDto);

            return "template-create";
        }

        tmpltVarApiService.create(requestDto);
        return "redirect:/smsTemplates/new";
    }

}
