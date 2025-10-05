package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemUpdateRequestDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
}
