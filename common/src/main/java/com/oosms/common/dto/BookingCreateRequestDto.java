package com.oosms.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BookingCreateRequestDto {
    @NotNull(message = "회원ID는 필수값 입니다.")
    private Long custId;

    @NotNull(message = "공연ID는 필수값 입니다.")
    private Long itemId;

    @NotNull(message = "예매 수량은 필수값 입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다")
    private Integer count;

}
