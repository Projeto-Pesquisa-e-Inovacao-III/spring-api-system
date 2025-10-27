package com.spring.ApiPag.dto.CheckoutDto;

import com.spring.ApiPag.enums.recurrancePlanUnit.RecurranceUnit;

public record RecurrancePlanIntervalDto(
        RecurranceUnit unit,
        Integer length
) {

}
