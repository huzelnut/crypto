package com.epam.crypto.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeTestUtils {

    public static Long getTimeStamp(int year, int month, int day, int hour, int minute, int second, int nanosecond) {
        var localDateTime = LocalDateTime.of(year, month, day, hour, minute, second, nanosecond);
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
