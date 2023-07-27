package com.epam.crypto.repository;

import com.epam.crypto.service.DateTimeService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * In-memory implementation for repository layer for working with prices and ranges
 */
@Component
public class InMemoryPriceRepository implements PriceRepository {

    private DateTimeService dateTimeService;

    private Map<String, NavigableMap<Long, BigDecimal>> storage;

    public InMemoryPriceRepository(DateTimeService dateTimeService,
                                   @Qualifier("priceStorage") Map<String, NavigableMap<Long, BigDecimal>> storage) {
        this.dateTimeService = dateTimeService;
        this.storage = storage;
    }

    /**
     * Get oldest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getOldestPrice(String currencySymbol) {
        return getOldestPrice(currencySymbol, getSubStorage(currencySymbol));
    }

    /**
     * Get oldest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getOldestPrice(String currencySymbol, int year, int month) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getOldestPrice(currencySymbol, subStorage);
    }

    /**
     * Get oldest price for the currency from map
     *
     * @param currencySymbol currency symbol
     * @param map            map of prices where keys are timestamps and values are prices
     * @return pair tuple of timestamp and price
     */
    private Pair<Long, BigDecimal> getOldestPrice(String currencySymbol, NavigableMap<Long, BigDecimal> map) {
        return Optional.ofNullable(map).stream()
                .filter(Objects::nonNull)
                .map(NavigableMap::firstEntry)
                .filter(Objects::nonNull)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .findAny()
                .orElse(null);
    }

    /**
     * Get newest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getNewestPrice(String currencySymbol) {
        return getNewestPrice(currencySymbol, getSubStorage(currencySymbol));
    }

    /**
     * Get newest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getNewestPrice(String currencySymbol, int year, int month) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getNewestPrice(currencySymbol, subStorage);
    }

    /**
     * Get newest price for the currency from map
     *
     * @param currencySymbol currency symbol
     * @param map            map of prices where keys are timestamps and values are prices
     * @return pair tuple of timestamp and price
     */
    private Pair<Long, BigDecimal> getNewestPrice(String currencySymbol, NavigableMap<Long, BigDecimal> map) {
        return Optional.ofNullable(map).stream()
                .filter(Objects::nonNull)
                .map(NavigableMap::lastEntry)
                .filter(Objects::nonNull)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .findAny()
                .orElse(null);
    }

    /**
     * Get lowest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getLowestPrice(String currencySymbol) {
        return getLowestPrice(currencySymbol, getSubStorage(currencySymbol));
    }

    /**
     * Get lowest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getLowestPrice(String currencySymbol, int year, int month) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getLowestPrice(currencySymbol, subStorage);
    }

    /**
     * Get lowest price for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getLowestPrice(String currencySymbol, int year, int month, int day) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month, day);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month, day);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getLowestPrice(currencySymbol, subStorage);
    }

    /**
     * Get lowest price for the currency from map
     *
     * @param currencySymbol currency symbol
     * @param map            map of prices where keys are timestamps and values are prices
     * @return pair tuple of timestamp and price
     */
    private Pair<Long, BigDecimal> getLowestPrice(String currencySymbol, NavigableMap<Long, BigDecimal> map) {
        return Optional.ofNullable(map).stream()
                .filter(Objects::nonNull)
                .flatMap(navigableMap -> navigableMap.entrySet().stream())
                .min(Comparator.comparing(Map.Entry::getValue))
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    /**
     * Get highest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getHighestPrice(String currencySymbol) {
        return getHighestPrice(currencySymbol, getSubStorage(currencySymbol));
    }

    /**
     * Get highest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getHighestPrice(String currencySymbol, int year, int month) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getHighestPrice(currencySymbol, subStorage);
    }

    /**
     * Get highest price for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return pair tuple of timestamp and price
     */
    @Override
    public Pair<Long, BigDecimal> getHighestPrice(String currencySymbol, int year, int month, int day) {
        long startTimestamp = dateTimeService.getStartTimeStamp(year, month, day);
        long endTimestamp = dateTimeService.getEndTimeStamp(year, month, day);
        var subStorage = getSubStorage(currencySymbol).subMap(startTimestamp, true, endTimestamp, true);
        return getHighestPrice(currencySymbol, subStorage);
    }

    /**
     * Get highest price for the currency from map
     *
     * @param currencySymbol currency symbol
     * @param map            map of prices where keys are timestamps and values are prices
     * @return pair tuple of timestamp and price
     */
    private Pair<Long, BigDecimal> getHighestPrice(String currencySymbol, NavigableMap<Long, BigDecimal> map) {
        return Optional.ofNullable(map).stream()
                .filter(Objects::nonNull)
                .flatMap(navigableMap -> navigableMap.entrySet().stream())
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    /**
     * Get normalized range for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return range
     */
    @Override
    public BigDecimal getNormalizedRange(String currencySymbol) {
        BigDecimal max = getPriceFromPair(getHighestPrice(currencySymbol));
        BigDecimal min = getPriceFromPair(getLowestPrice(currencySymbol));
        if (max == null || min == null) {
            return null;
        }
        return max.add(min).divide(min, 6, RoundingMode.HALF_UP);
    }

    /**
     * Get normalized range for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return range
     */
    @Override
    public BigDecimal getNormalizedRange(String currencySymbol, int year, int month, int day) {
        BigDecimal max = getPriceFromPair(getHighestPrice(currencySymbol, year, month, day));
        BigDecimal min = getPriceFromPair(getLowestPrice(currencySymbol, year, month, day));
        if (max == null || min == null) {
            return null;
        }
        return max.add(min).divide(min, 6, RoundingMode.HALF_UP);
    }

    /**
     * Get set of all currencies symbols that represented in storage
     *
     * @return set of currencies symbols
     */
    @Override
    public Set<String> getCurrencies() {
        return this.storage.keySet();
    }

    /**
     * Return sub-storage for defined currency
     *
     * @param currencySymbol currency symbol
     * @return sub-storage (map) for defined currency
     */
    private NavigableMap<Long, BigDecimal> getSubStorage(String currencySymbol) {
        return this.storage.get(currencySymbol);
    }

    /**
     * Extract price from pair tuple
     *
     * @param pair pair tuple with timestamp and price
     * @return price from tuple (if tuple is not null),
     * null if tuple is null or price is null
     */
    public BigDecimal getPriceFromPair(Pair<Long, BigDecimal> pair) {
        return Optional.ofNullable(pair)
                .stream()
                .filter(Objects::nonNull)
                .map(Pair::getRight)
                .findAny()
                .orElse(null);
    }
}
