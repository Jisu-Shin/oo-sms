package com.oosms.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemCreateRequestDto {
    @NotEmpty @NotBlank(message = "공연명은 필수값입니다.")
    private String name;

    @NotEmpty @NotBlank(message = "공연가격은 필수값입니다.")
    private String price;

    @NotEmpty @NotBlank(message = "공연재고수량은 필수값입니다.")
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
