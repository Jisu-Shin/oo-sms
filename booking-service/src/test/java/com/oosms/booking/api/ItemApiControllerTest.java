package com.oosms.booking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oosms.booking.mapper.ItemMapper;
import com.oosms.booking.repository.JpaItemRepository;
import com.oosms.booking.service.ItemService;
import com.oosms.common.dto.CustSaveRequestDto;
import com.oosms.common.dto.ItemCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemApiController.class)
class ItemApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemMapper itemMapper;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private JpaItemRepository repository;

    @Test
    public void 공연등록() throws Exception {
        //given
        ItemCreateRequestDto requestDto = new ItemCreateRequestDto();
        requestDto.setName("[뮤지컬] 비틀쥬스");
        requestDto.setPrice("100,000");
        requestDto.setStockQuantity("100");

        when(itemService.saveItem(any())).thenReturn(1L);

        //when & then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }
}