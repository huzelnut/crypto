package com.epam.crypto.service;

import com.epam.crypto.repository.PriceRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Test
public class PriceServiceTest {

    private static final String CURR_SYMBOL_BTC = "BTC";
    private static final String CURR_SYMBOL_DOGE = "DOGE";

    private static final int YEAR = 2022;
    private static final int MONTH = 1;
    private static final int DAY = 1;

    private static final int YEAR_NON_EXISTING = 2023;
    private static final int MONTH_NON_EXISTING = 2;
    private static final int DAY_NON_EXISTING = 2;

    private static final BigDecimal RANGE_BTC = new BigDecimal("2.000999");

    private static final BigDecimal PRICE_LOWEST = new BigDecimal("10.01");
    private static final BigDecimal PRICE_HIGHEST = new BigDecimal("10.05");
    private static final BigDecimal PRICE_OLDEST = new BigDecimal("10.02");
    private static final BigDecimal PRICE_NEWEST = new BigDecimal("10.03");

    // Timestamp of 2022-01-01 01:01:01.0001 UTC+0

    private static final long TIMESTAMP_FIRST = 1640998861001L;

    // Timestamp of 2022-01-01 01:01:01.0002 UTC+0
    private static final long TIMESTAMP_SECOND = 1640998861002L;

    // Timestamp of 2022-01-01 01:01:01.0003 UTC+0
    private static final long TIMESTAMP_THIRD = 1640998861003L;

    // Timestamp of 2022-01-01 01:01:01.0004 UTC+0
    private static final long TIMESTAMP_FORTH = 1640998861004L;

    private PriceService priceService;

    @BeforeClass
    public void init() {
        PriceRepository priceRepository = mock(PriceRepository.class);

        when(priceRepository.getCurrencies()).thenReturn(Set.of(CURR_SYMBOL_BTC));
        when(priceRepository.getNormalizedRange(CURR_SYMBOL_BTC)).thenReturn(RANGE_BTC);

        when(priceRepository.getNormalizedRange(CURR_SYMBOL_BTC, YEAR, MONTH, DAY)).thenReturn(RANGE_BTC);
        when(priceRepository.getNormalizedRange(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING, DAY_NON_EXISTING))
                .thenReturn(null);
        when(priceRepository.getNormalizedRange(CURR_SYMBOL_DOGE, YEAR_NON_EXISTING, MONTH_NON_EXISTING, DAY_NON_EXISTING))
                .thenReturn(null);

        when(priceRepository.getLowestPrice(CURR_SYMBOL_BTC)).thenReturn(Pair.of(TIMESTAMP_SECOND, PRICE_LOWEST));
        when(priceRepository.getHighestPrice(CURR_SYMBOL_BTC)).thenReturn(Pair.of(TIMESTAMP_THIRD, PRICE_HIGHEST));
        when(priceRepository.getOldestPrice(CURR_SYMBOL_BTC)).thenReturn(Pair.of(TIMESTAMP_FIRST, PRICE_OLDEST));
        when(priceRepository.getNewestPrice(CURR_SYMBOL_BTC)).thenReturn(Pair.of(TIMESTAMP_FORTH, PRICE_NEWEST));

        when(priceRepository.getLowestPrice(CURR_SYMBOL_DOGE)).thenReturn(null);
        when(priceRepository.getHighestPrice(CURR_SYMBOL_DOGE)).thenReturn(null);
        when(priceRepository.getOldestPrice(CURR_SYMBOL_DOGE)).thenReturn(null);
        when(priceRepository.getHighestPrice(CURR_SYMBOL_DOGE)).thenReturn(null);

        priceService = new PriceService(priceRepository);
    }

    @Test
    public void test0010_getRanges() {
        // When
        var ranges = priceService.getRanges();

        // Then
        assertNotNull(ranges);
        assertTrue(ranges.size() > 0);
        assertEquals(CURR_SYMBOL_BTC, ranges.get(0).getCurrencySymbol());
        assertTrue(RANGE_BTC.compareTo(ranges.get(0).getRange()) == 0);
    }

    @Test
    public void test0020_getHighestRangeOnExistingDate() {
        // When
        var range = priceService.getHighestRangeOnDate(LocalDate.of(YEAR, MONTH, DAY));

        // Then
        assertNotNull(range);
        assertEquals(CURR_SYMBOL_BTC, range.getCurrencySymbol());
        assertTrue(RANGE_BTC.compareTo(range.getRange()) == 0);
    }

    @Test
    public void test0021_getHighestRangeOnNonExistingDate() {
        assertThrows(RuntimeException.class,
                () -> priceService.getHighestRangeOnDate(LocalDate.of(YEAR_NON_EXISTING, MONTH_NON_EXISTING, DAY_NON_EXISTING)));
    }

    @Test
    public void test0030_getPricesOnExistingCurrency() {
        // When
        var prices = priceService.getPrices(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(prices);
        assertEquals(CURR_SYMBOL_BTC, prices.getCurrencySymbol());
        assertEquals(PRICE_LOWEST, prices.getMin());
        assertEquals(PRICE_HIGHEST, prices.getMax());
        assertEquals(PRICE_OLDEST, prices.getOldest());
        assertEquals(PRICE_NEWEST, prices.getNewest());
    }

    @Test
    public void test0031_getPricesOnNonExistingCurrency() {
        // When
        var prices = priceService.getPrices(CURR_SYMBOL_DOGE);

        // Then
        assertNotNull(prices);
        assertEquals(CURR_SYMBOL_DOGE, prices.getCurrencySymbol());
        assertNull(prices.getMin());
        assertNull(prices.getMax());
        assertNull(prices.getOldest());
        assertNull(prices.getNewest());
    }
}