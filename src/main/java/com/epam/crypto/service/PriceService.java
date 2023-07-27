package com.epam.crypto.service;

import com.epam.crypto.model.PriceDto;
import com.epam.crypto.model.RangeDto;
import com.epam.crypto.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service to perform operation with prices and ranges
 */
@Service
@AllArgsConstructor
public class PriceService {

    private PriceRepository priceRepository;

    /**
     * Get normalized ranges for all currencies (for all time)
     *
     * @return list of ranges
     */
    public List<RangeDto> getRanges() {
        return priceRepository.getCurrencies()
                .stream()
                .map(currencySymbol -> RangeDto.builder()
                        .currencySymbol(currencySymbol)
                        .range(priceRepository.getNormalizedRange(currencySymbol))
                        .build())
                .sorted(Comparator.comparing(RangeDto::getRange).reversed())
                .collect(Collectors.toList());
    }


    /**
     * Get highest normalized range on date
     *
     * @param date date on which ranges must be calculated
     * @return highest range on date
     */
    public RangeDto getHighestRangeOnDate(LocalDate date) {
        return priceRepository.getCurrencies()
                .stream()
                .map(currencySymbol -> RangeDto.builder()
                        .currencySymbol(currencySymbol)
                        .range(priceRepository.getNormalizedRange(currencySymbol, date.getYear(), date.getMonthValue(), date.getDayOfMonth()))
                        .build())
                .filter(rangeDto -> Objects.nonNull(rangeDto.getRange()))
                .max(Comparator.comparing(RangeDto::getRange))
                .orElseThrow(() -> new RuntimeException("No data found"));
    }

    /**
     * Get oldest, newest, minimum and maximum prices for currency
     *
     * @param currencySymbol currency symbol
     * @return prices DTO
     */
    public PriceDto getPrices(String currencySymbol) {
        return PriceDto.builder()
                .currencySymbol(currencySymbol)
                .oldest(getPriceFromPair(priceRepository.getOldestPrice(currencySymbol)))
                .newest(getPriceFromPair(priceRepository.getNewestPrice(currencySymbol)))
                .min(getPriceFromPair(priceRepository.getLowestPrice(currencySymbol)))
                .max(getPriceFromPair(priceRepository.getHighestPrice(currencySymbol)))
                .build();
    }

    /**
     * Extract price from pair tuple
     *
     * @param pair pair tuple with timestamp and price
     * @return price from tuple (if tuple is not null),
     * null if tuple is null or price is null
     */
    private BigDecimal getPriceFromPair(Pair<Long, BigDecimal> pair) {
        return Optional.ofNullable(pair)
                .stream()
                .filter(Objects::nonNull)
                .map(Pair::getRight)
                .findAny()
                .orElse(null);
    }
}
