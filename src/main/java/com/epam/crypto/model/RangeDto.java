package com.epam.crypto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class RangeDto {

    @Schema(description = "Currency symbol", example = "BTC")
    private String currencySymbol;

    @Schema(description = "Normalized range value", example = "2.285677")
    private BigDecimal range;
}
