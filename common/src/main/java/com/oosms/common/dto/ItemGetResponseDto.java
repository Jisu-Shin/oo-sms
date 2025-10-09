package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemGetResponseDto {
    private Long id;
    private String name;
    private String price;
    private int stockQuantity;

}
