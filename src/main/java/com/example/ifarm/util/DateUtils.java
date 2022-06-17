package com.example.ifarm.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // get current date in YYYY-MM-DD format
    public static String getCurrentDateString() {
        return LocalDate.now().format(formatter);
    }
}
