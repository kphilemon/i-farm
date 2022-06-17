package com.example.ifarm.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // get current date in YYYY-MM-DD format
    public static String getCurrentDateString() {
        return LocalDate.now().format(formatter);
    }

    public static boolean validateDateString(String date) {
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
