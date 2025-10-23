package com.oosms.view.controller;

import com.oosms.common.dto.SmsListSearchDto;
import com.oosms.view.client.ItemApiService;
import com.oosms.view.client.SmsApiService;
import com.oosms.view.client.SmsTemplateApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
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
    public String getSmsList(Model model, @ModelAttribute("smsListSearchDto") SmsListSearchDto smsListSearchDto) {
        log.info("##### SmsController.getSmsList\n\n");

        String startDate = smsListSearchDto.getStartDate() == null ? getSevenDaysAgo() : smsListSearchDto.getStartDate();
        String endDate = smsListSearchDto.getEndDate() == null ? getToday() : smsListSearchDto.getEndDate();

        String startDateTime = startDate.replaceAll("-", "").concat("0000");
        String endDateTime = endDate.replaceAll("-", "").concat("2359");

        log.info("##### startDateTime = {}", startDateTime);
        log.info("##### endDateTime = {}",endDateTime);

        smsListSearchDto.setStartDate(startDateTime);
        smsListSearchDto.setEndDate(endDateTime);

        model.addAttribute("sms", smsApiService.getSmsList(smsListSearchDto));
        return "sms-sendlist";
    }

    private String getSevenDaysAgo() {
        return LocalDate.now().minusDays(7).toString();
    }

    private String getToday() {
        return LocalDate.now().toString();
    }
}
