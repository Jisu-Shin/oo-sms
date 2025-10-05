package com.oosms.view.controller.env_test;

import com.oosms.common.dto.BookingCreateRequestDto;
import com.oosms.common.dto.BookingSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Slf4j
@Controller
@Profile("test")
@RequestMapping("/view/bookings")
public class BookingController {

    @GetMapping("")
    public String bookingList(Model model, @ModelAttribute("bookingSearch") BookingSearch bookingSearch) {
        log.info("######bookingController.bookingList 진입");

        model.addAttribute("bookings", Collections.emptyList());
        model.addAttribute("bookingSearch", bookingSearch);
        return "booking-getList";
    }

    @GetMapping("/new")
    public String createBookingForm(Model model) {
        log.info("######bookingController.createBookingForm 진입");

        model.addAttribute("custs", Collections.emptyList());
        model.addAttribute("items", Collections.emptyList());

        return "booking-createForm";
    }

    @PostMapping("/new")
    public String booking(BookingCreateRequestDto requestDto) {
        return "redirect:/";
    }
}
