package com.epam.crypto.repository;

import com.epam.crypto.service.DateTimeService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@Test
public class InMemoryPriceRepositoryTest {

    private static final String CURR_SYMBOL_BTC = "BTC";
    private static final String CURR_SYMBOL_ETH = "ETH";
    private static final String CURR_SYMBOL_DOGE = "DOGE";

    private static final int YEAR = 2022;
    private static final int MONTH = 1;
    private static final int DAY = 1;

    private static final int YEAR_NON_EXISTING = 2023;
    private static final int MONTH_NON_EXISTING = 2;
    private static final int DAY_NON_EXISTING = 2;

    // Timestamp of 2022-01-01 01:01:01.0001 UTC+0
    private static final long TIMESTAMP_FIRST = 1640998861001L;

    // Timestamp of 2022-01-01 01:01:01.0002 UTC+0
    private static final long TIMESTAMP_SECOND = 1640998861002L;

    // Timestamp of 2022-01-01 01:01:01.0003 UTC+0
    private static final long TIMESTAMP_THIRD = 1640998861003L;

    private static final BigDecimal PRICE_FIRST = new BigDecimal(10.01);
    private static final BigDecimal PRICE_SECOND = new BigDecimal(10.02);
    private static final BigDecimal PRICE_THIRD = new BigDecimal(10.03);

    private static final BigDecimal RANGE_BTC = new BigDecimal("2.000999");

    private InMemoryPriceRepository repository;

    @BeforeClass
    public void init() {
        DateTimeService dateTimeService = new DateTimeService();
        var storage = new HashMap<String, NavigableMap<Long, BigDecimal>>();

        storage.put(CURR_SYMBOL_BTC, new TreeMap<>());
        storage.put(CURR_SYMBOL_ETH, new TreeMap<>());

        storage.get(CURR_SYMBOL_BTC).put(TIMESTAMP_FIRST, PRICE_FIRST);
        storage.get(CURR_SYMBOL_BTC).put(TIMESTAMP_SECOND, PRICE_SECOND);

        storage.get(CURR_SYMBOL_ETH).put(TIMESTAMP_SECOND, PRICE_SECOND);
        storage.get(CURR_SYMBOL_ETH).put(TIMESTAMP_THIRD, PRICE_THIRD);

        repository = new InMemoryPriceRepository(dateTimeService, storage);
    }

    @Test
    public void test0010_getOldestPriceOnExistingCurrency() {
        // When
        var found = repository.getOldestPrice(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_FIRST);
        assertEquals(found.getValue(), PRICE_FIRST);
    }

    @Test
    public void test0011_getOldestPriceOnExistingCurrencyOnExistingDate() {
        // When
        var found = repository.getOldestPrice(CURR_SYMBOL_BTC, YEAR, MONTH);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_FIRST);
        assertEquals(found.getValue(), PRICE_FIRST);
    }

    @Test
    public void test0012_getOldestPriceOnExistingCurrencyOnNonExistingDate() {
        // When
        var found = repository.getOldestPrice(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING);

        // Then
        assertNull(found);
    }

    @Test
    public void test0013_getOldestPriceOnNonExistingCurrency() {
        // When
        var found = repository.getOldestPrice(CURR_SYMBOL_DOGE);

        // Then
        assertNull(found);
    }

    @Test
    public void test0020_getNewestPriceOnExistingCurrency() {
        // When
        var found = repository.getNewestPrice(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_SECOND);
        assertEquals(found.getValue(), PRICE_SECOND);
    }

    @Test
    public void test0021_getNewestPriceOnExistingCurrencyOnExistingDate() {
        // When
        var found = repository.getNewestPrice(CURR_SYMBOL_BTC, YEAR, MONTH);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_SECOND);
        assertEquals(found.getValue(), PRICE_SECOND);
    }

    @Test
    public void test0022_getNewestPriceOnExistingCurrencyOnNonExistingDate() {
        // When
        var found = repository.getNewestPrice(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING);

        // Then
        assertNull(found);
    }

    @Test
    public void test0023_getNewestPriceOnNonExistingCurrency() {
        // When
        var found = repository.getNewestPrice(CURR_SYMBOL_DOGE);

        // Then
        assertNull(found);
    }

    @Test
    public void test0030_getLowestPriceOnExistingCurrency() {
        // When
        var found = repository.getLowestPrice(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_FIRST);
        assertEquals(found.getValue(), PRICE_FIRST);
    }

    @Test
    public void test0031_getLowestPriceOnExistingCurrencyOnExistingDate() {
        // When
        var found = repository.getLowestPrice(CURR_SYMBOL_BTC, YEAR, MONTH);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_FIRST);
        assertEquals(found.getValue(), PRICE_FIRST);
    }

    @Test
    public void test0032_getLowestPriceOnExistingCurrencyOnNonExistingDate() {
        // When
        var found = repository.getLowestPrice(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING);

        // Then
        assertNull(found);
    }

    @Test
    public void test0033_getLowestPriceOnNonExistingCurrency() {
        // When
        var found = repository.getLowestPrice(CURR_SYMBOL_DOGE);

        // Then
        assertNull(found);
    }

    @Test
    public void test0040_getHighestPriceOnExistingCurrency() {
        // When
        var found = repository.getHighestPrice(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_SECOND);
        assertEquals(found.getValue(), PRICE_SECOND);
    }

    @Test
    public void test0041_getHighestPriceOnExistingCurrencyOnExistingDate() {
        // When
        var found = repository.getHighestPrice(CURR_SYMBOL_BTC, YEAR, MONTH);

        // Then
        assertNotNull(found);
        assertEquals(found.getKey(), TIMESTAMP_SECOND);
        assertEquals(found.getValue(), PRICE_SECOND);
    }

    @Test
    public void test0042_getHighestPriceOnExistingCurrencyOnNonExistingDate() {
        // When
        var found = repository.getHighestPrice(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING);

        // Then
        assertNull(found);
    }

    @Test
    public void test0043_getHighestPriceOnNonExistingCurrency() {
        // When
        var found = repository.getHighestPrice(CURR_SYMBOL_DOGE);

        // Then
        assertNull(found);
    }

    @Test
    public void test0050_getCurrencies() {
        // When
        var found = repository.getCurrencies();

        // Then
        assertNotNull(found);
        assertTrue(found.contains(CURR_SYMBOL_BTC));
        assertTrue(found.contains(CURR_SYMBOL_ETH));
        assertFalse(found.contains(CURR_SYMBOL_DOGE));
    }

    @Test
    public void test0060_getRangeOfExistingCurrency() {
        // When
        var found = repository.getNormalizedRange(CURR_SYMBOL_BTC);

        // Then
        assertNotNull(found);
        assertTrue(found.compareTo(RANGE_BTC) == 0);
    }

    @Test
    public void test0061_getRangeOfExistingCurrencyOnExistingDay() {
        // When
        var found = repository.getNormalizedRange(CURR_SYMBOL_BTC, YEAR, MONTH, DAY);

        // Then
        assertNotNull(found);
        assertTrue(found.compareTo(RANGE_BTC) == 0);
    }

    @Test
    public void test0062_getRangeOfExistingCurrencyOnNonExistingDay() {
        // When
        var found = repository.getNormalizedRange(CURR_SYMBOL_BTC, YEAR_NON_EXISTING, MONTH_NON_EXISTING, DAY_NON_EXISTING);

        // Then
        assertNull(found);
    }

    @Test
    public void test0063_getRangeOfNonExistingCurrency() {
        // When
        var found = repository.getNormalizedRange(CURR_SYMBOL_DOGE);

        // Then
        assertNull(found);
    }
}