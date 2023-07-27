package com.epam.crypto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class PriceDto {

    @Schema(description = "Currency symbol", example = "BTC")
    private String currencySymbol;

    @Schema(description = "Oldest price", example = "10.127")
    private BigDecimal oldest;

    @Schema(description = "Newest price", example = "10.128")
    private BigDecimal newest;

    @Schema(description = "Min price", example = "9.988")
    private BigDecimal min;

    @Schema(description = "Max price", example = "11.123")
    private BigDecimal max;
}
