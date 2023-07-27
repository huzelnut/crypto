package com.epam.crypto.controller;

import com.epam.crypto.model.PriceDto;
import com.epam.crypto.model.RangeDto;
import com.epam.crypto.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/prices")
@AllArgsConstructor
@Tag(name = "Price controller", description = "Endpoints for getting prices and normalized ranges")
public class PriceController {

    private PriceService priceService;

    @Operation(summary = "Get ranges for all cryptocurrencies for the whole time")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Response was collected with no issues",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RangeDto.class)))
            )
    })
    @GetMapping("/ranges")
    ResponseEntity<List<RangeDto>> getRanges() {
        return ResponseEntity.ok(priceService.getRanges());
    }

    @Operation(summary = "Get highest normalized range on date")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Response was collected with no issues",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RangeDto.class))
            )
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "date", description = "Date in 'YYYY-MM-DD' format", schema = @Schema(type = "string"), example = "2022-01-01"),
    })
    @GetMapping("/ranges/highest")
    ResponseEntity<RangeDto> getHighestRangeOnDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(priceService.getHighestRangeOnDate(date));
    }

    @Operation(summary = "Get newest, oldest, max and min prices for the currency")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Response was collected with no issues",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDto.class))
            )
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "currencySymbol", description = "Currency symbol", schema = @Schema(type = "string"), example = "BTC"),
    })
    @GetMapping
    ResponseEntity<PriceDto> getPricesForCurrency(@RequestParam String currencySymbol) {
        return ResponseEntity.ok(priceService.getPrices(currencySymbol));
    }
}
