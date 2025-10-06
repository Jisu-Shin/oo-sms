package com.oosms.view.controller.env_test;

import com.oosms.common.dto.CustSaveRequestDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@Slf4j
@Profile("test")
@RequestMapping("/custs")
public class CustController {

    @GetMapping("")
    public String getCustList(Model model) {
        model.addAttribute("custs", Collections.emptyList());
        return "cust-findAll";
    }

    @GetMapping("/new")
    public String createCust() {
        return "cust-createForm";
    }

    @PostMapping("/new")
    public String save(@Valid CustSaveRequestDto requestDto, BindingResult result, Model model) {
        return "redirect:/custs";
    }
}
