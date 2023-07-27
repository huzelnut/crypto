package com.epam.crypto.repository;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Abstraction for repository layer for working with prices and ranges
 */
public interface PriceRepository {

    /**
     * Get oldest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getOldestPrice(String currencySymbol);

    /**
     * Get oldest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getOldestPrice(String currencySymbol, int year, int month);

    /**
     * Get newest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getNewestPrice(String currencySymbol);

    /**
     * Get newest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getNewestPrice(String currencySymbol, int year, int month);

    /**
     * Get lowest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getLowestPrice(String currencySymbol);

    /**
     * Get lowest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getLowestPrice(String currencySymbol, int year, int month);

    /**
     * Get lowest price for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getLowestPrice(String currencySymbol, int year, int month, int day);

    /**
     * Get highest price for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getHighestPrice(String currencySymbol);

    /**
     * Get highest price for the currency for the month
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getHighestPrice(String currencySymbol, int year, int month);

    /**
     * Get highest price for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return pair tuple of timestamp and price
     */
    Pair<Long, BigDecimal> getHighestPrice(String currencySymbol, int year, int month, int day);

    /**
     * Get normalized range for the currency for all time
     *
     * @param currencySymbol currency symbol
     * @return range
     */
    BigDecimal getNormalizedRange(String currencySymbol);

    /**
     * Get normalized range for the currency for the day
     *
     * @param currencySymbol currency symbol
     * @param year           year from -999999999 to 999999999
     * @param month          month from 1 to 12
     * @param day            day from 1 to 28..31 (validity depends on year and month)
     * @return range
     */
    BigDecimal getNormalizedRange(String currencySymbol, int year, int month, int day);

    /**
     * Get set of all currencies symbols that represented in storage
     *
     * @return set of currencies symbols
     */
    Set<String> getCurrencies();
}
