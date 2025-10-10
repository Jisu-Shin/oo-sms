package com.oosms.view.controller;



import com.oosms.common.dto.ErrorResponseDto;
import com.oosms.view.client.BookingApiService;
import com.oosms.view.client.CustApiService;
import com.oosms.view.client.ItemApiService;
import com.oosms.common.dto.BookingCreateRequestDto;
import com.oosms.common.dto.BookingListResponseDto;
import com.oosms.common.dto.BookingSearch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@Profile("prod")
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingApiService bookingApiService;
    private final ItemApiService itemApiService;
    private final CustApiService custApiService;

    @GetMapping("")
    public String bookingList(Model model, @ModelAttribute("bookingSearch") BookingSearch bookingSearch) {
        log.info("######bookingController.bookingList 진입");

        List<BookingListResponseDto> searchList = bookingApiService.searchBooking(bookingSearch);

        model.addAttribute("bookings", searchList);
        model.addAttribute("bookingSearch", bookingSearch);
        return "booking-getList";
    }

    @GetMapping("/new")
    public String createBookingForm(Model model) {
        log.info("######bookingController.createBookingForm 진입");

        model.addAttribute("custs", custApiService.getCustAll());
        model.addAttribute("items", itemApiService.getItemAll());

        return "booking-createForm";
    }

    @PostMapping("/new")
    public String booking(@Valid BookingCreateRequestDto requestDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, ErrorResponseDto> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(),
                            new ErrorResponseDto(true, error.getDefaultMessage())));

            model.addAttribute("errors", errors);
            model.addAttribute("requestDto", requestDto);
            return "booking-createForm";
        }

        bookingApiService.book(requestDto);
        return "redirect:/";
    }
}
