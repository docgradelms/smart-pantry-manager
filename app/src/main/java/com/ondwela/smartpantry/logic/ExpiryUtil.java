package com.ondwela.smartpantry.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/** Helpers for the yyyy-MM-dd expiry dates stored with pantry items. */
public final class ExpiryUtil {

    public static final String FORMAT = "yyyy-MM-dd";
    /** An ingredient counts as "expiring soon" this many days before its date. */
    public static final int SOON_DAYS = 3;

    private ExpiryUtil() { }

    /** Whole days from today until the date (negative when already expired), or null if no date. */
    public static Integer daysUntil(String expiry) {
        if (expiry == null || expiry.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(FORMAT, Locale.US);
            fmt.setLenient(false);
            Date target = fmt.parse(expiry);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            long diff = target.getTime() - today.getTimeInMillis();
            return (int) TimeUnit.MILLISECONDS.toDays(diff);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isExpired(String expiry) {
        Integer d = daysUntil(expiry);
        return d != null && d < 0;
    }

    public static boolean isExpiringSoon(String expiry) {
        Integer d = daysUntil(expiry);
        return d != null && d >= 0 && d <= SOON_DAYS;
    }

    public static String format(int year, int month, int day) {
        return String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
    }
}
