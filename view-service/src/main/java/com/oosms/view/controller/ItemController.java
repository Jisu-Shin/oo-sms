package com.oosms.view.controller;


import com.oosms.common.dto.ErrorResponseDto;
import com.oosms.view.client.ItemApiService;
import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@Profile("prod")
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemApiService itemApiService;

    @GetMapping("")
    public String getItemList(Model model) {
        log.info("###########ItemController getItemList 진입");
        model.addAttribute("items", itemApiService.getItemAll());
        return "item-getList";
    }

    @GetMapping("/new")
    public String createItemForm() {
        return "item-createForm";
    }

    @PostMapping("/new")
    public String createItem(@Valid ItemCreateRequestDto requestDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, ErrorResponseDto> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(),
                            new ErrorResponseDto(true, error.getDefaultMessage()))
                    );
            model.addAttribute("errors", errors);
            model.addAttribute("requestDto", requestDto);
            return "item-createForm";
        }
        itemApiService.createItem(requestDto);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String updateItemForm(@PathVariable("id")Long id, Model model) {
        model.addAttribute("item", itemApiService.getItem(id));
        return "item-updateForm";
    }

    @PostMapping("/{id}")
    public String updateItem(ItemUpdateRequestDto requestDto) {
        itemApiService.updateItem(requestDto);

        return "redirect:/items";
    }

}
