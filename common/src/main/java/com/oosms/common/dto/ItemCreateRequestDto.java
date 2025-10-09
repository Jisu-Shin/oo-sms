package com.oosms.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemCreateRequestDto {
    @NotNull @NotEmpty
    private String name;

    @NotNull @NotEmpty
    private String price;

    @NotNull @NotEmpty
    private String stockQuantity;

    @Override
    public String toString() {
        return "ItemCreateRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
