package com.oosms.view.controller;

import com.oosms.view.client.ItemApiService;
import com.oosms.view.client.SmsApiService;
import com.oosms.view.client.SmsTemplateApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Profile("prod")
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsApiService smsApiService;
    private final SmsTemplateApiService smsTemplateApiService;
    private final ItemApiService itemApiService;

    @GetMapping("/send")
    public String sendSms(Model model) {
        model.addAttribute("templates", smsTemplateApiService.getSmsTemplates());
        model.addAttribute("items", itemApiService.getItemAll());

        return "sms-sendForm";
    }

    @GetMapping("/sendList")
    public String getSmsList(Model model) {
        LocalDate startDt = LocalDate.now().minusDays(6);
        String parseStatDt = startDt.format(DateTimeFormatter.ofPattern("yyyyMMdd")).concat("0000");

        LocalDate endDt = LocalDate.now().plusDays(1);
        String parseEndDt = endDt.format(DateTimeFormatter.ofPattern("yyyyMMdd")).concat("0000");

        model.addAttribute("sms", smsApiService.getSmsList(parseStatDt, parseEndDt));
        return "sms-sendlist";
    }
}
