package com.oosms.view.controller.env_test;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@Profile("test")
@RequestMapping("/view/sms")
public class SmsController {

     @GetMapping("/send")
    public String sendSms(Model model) {
        model.addAttribute("templates", Collections.emptyList());
        model.addAttribute("items", Collections.emptyList());

        return "sms-sendForm";
    }

    @GetMapping("/sendList")
    public String getSmsList(Model model) {
       model.addAttribute("sms", Collections.emptyList());
        return "sms-sendlist";
    }
}
