package com.oosms.view.controller.env_test;

import com.oosms.common.dto.SmsTemplateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@Slf4j
@Profile("test")
@RequiredArgsConstructor
@RequestMapping("/view/smsTemplates")
public class SmsTemplateController {

    @GetMapping("/new")
    public String createTemplateForm(Model model) {
        model.addAttribute("templates", Collections.emptyList());
        model.addAttribute("placeholders", Collections.emptyList());
        return "template-create";
    }

    @PostMapping("/new")
    public String createTemplate(SmsTemplateRequestDto requestDto) {
        return "redirect:/smsTemplates/new";
    }

}
