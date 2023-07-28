package com.epam.crypto.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@Service
public class DateTimeService {

    private static final int FIRST_DAY_OF_MONTH = 1;

    private static final int LAST_HOUR = 23;
    private static final int LAST_MINUTE = 59;
    private static final int LAST_SECOND = 59;
    private static final int LAST_NANOSECOND = 999_999_999;

    /**
     * Get timestamp of start of defined month
     *
     * @param year  year from -999999999 to 99999999
     * @param month month from 1 to 12
     * @return milliseconds
     */
    public Long getStartTimeStamp(int year, int month) {
        return getStartTimeStamp(year, month, FIRST_DAY_OF_MONTH);
    }


    /**
     * Get timestamp of start of defined day
     *
     * @param year  year from -999999999 to 99999999
     * @param month month from 1 to 12
     * @param day   day from 1 to 28..31 (validity depends on month and year)
     * @return milliseconds
     */
    public Long getStartTimeStamp(int year, int month, int day) {
        return localDateTimeToSeconds(LocalDate.of(year, month, day).atStartOfDay());
    }

    /**
     * Get timestamp of start of defined day
     *
     * @param year  year from -999999999 to 99999999
     * @param month month from 1 to 12
     * @return milliseconds
     */
    public Long getEndTimeStamp(int year, int month) {
        LocalDateTime localDateTime = YearMonth.of(year, month)
                .atEndOfMonth()
                .atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND, LAST_NANOSECOND);
        return localDateTimeToSeconds(localDateTime);
    }


    /**
     * Get timestamp of end of defined day
     *
     * @param year  year from -999999999 to 99999999
     * @param month month from 1 to 12
     * @param day   day from 1 to 28..31 (validity depends on month and year)
     * @return milliseconds
     */
    public Long getEndTimeStamp(int year, int month, int day) {
        LocalDateTime localDateTime = LocalDate.of(year, month, day).atTime(LAST_HOUR, LAST_MINUTE, LAST_SECOND, LAST_NANOSECOND);
        return localDateTimeToSeconds(localDateTime);
    }

    /**
     * Convert local datetime to milliseconds (in UTC+0 timezone)
     *
     * @param source source local datetime
     * @return milliseconds
     */
    private Long localDateTimeToSeconds(LocalDateTime source) {
        return Optional.ofNullable(source).stream()
                .filter(Objects::nonNull)
                .map(localDateTime -> localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli())
                .findFirst()
                .orElse(null);
    }
}
