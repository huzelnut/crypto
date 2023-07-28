package com.epam.crypto.service;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Test
public class DateTimeServiceTest {

    private static final int YEAR = 2022;
    private static final int MONTH = 1;
    private static final int DAY = 15;

    private DateTimeService service;

    @BeforeClass
    public void init() {
        service = new DateTimeService();
    }

    @Test
    public void test0010_getStartTimeStampOfMonth() {
        // When
        Long timestamp = service.getStartTimeStamp(YEAR, MONTH);

        // Then
        assertNotNull(timestamp);
        assertEquals(timestamp, 1640995200000L);
    }

    @Test
    public void test0020_getStartTimeStampOfDay() {
        // When
        Long timestamp = service.getStartTimeStamp(YEAR, MONTH, DAY);

        // Then
        assertNotNull(timestamp);
        assertEquals(timestamp, 1642204800000L);
    }

    @Test
    public void test0030_getEndTimeStampOfMonth() {
        // When
        Long timestamp = service.getEndTimeStamp(YEAR, MONTH);

        // Then
        assertNotNull(timestamp);
        assertEquals(timestamp, 1643673599999L);
    }

    @Test
    public void test0040_getEndTimeStampOfDay() {
        // When
        Long timestamp = service.getEndTimeStamp(YEAR, MONTH, DAY);

        // Then
        assertNotNull(timestamp);
        assertEquals(timestamp, 1642291199999L);
    }
}