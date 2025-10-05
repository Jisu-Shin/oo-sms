package com.oosms.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemCreateRequestDto {
    private String name;
    private int price;
    private int stockQuantity;


    @Override
    public String toString() {
        return "ItemCreateRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
