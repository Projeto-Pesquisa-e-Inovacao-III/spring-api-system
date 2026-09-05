package com.spring.ApiSystem.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class MoneyUtil {

    public static Integer toCents(Double value) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }
}
