package com.oosms.view.controller.env_test;

import com.oosms.common.dto.ItemCreateRequestDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Slf4j
@Controller
@Profile("test")
@RequestMapping("/view/items")
public class ItemController {

    @GetMapping("")
    public String getItemList(Model model) {
        log.info("###########ItemController getItemList 진입");
        model.addAttribute("items", Collections.emptyList());
        return "item-getList";
    }

    @GetMapping("/new")
    public String createItemForm() {
        return "item-createForm";
    }

    @PostMapping("/new")
    public String createItem(ItemCreateRequestDto requestDto) {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String updateItemForm(@PathVariable("id")Long id, Model model) {
        model.addAttribute("item", Collections.emptyList());
        return "item-updateForm";
    }

    @PostMapping("/{id}")
    public String updateItem(ItemUpdateRequestDto requestDto) {
        return "redirect:/items";
    }

}
