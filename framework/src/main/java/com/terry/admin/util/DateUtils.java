package com.terry.admin.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    private static final DateTimeFormatter yyyy_MM_ss_HHmmss_dft = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter yyyy_MM_ss_dft = DateTimeFormat.forPattern("yyyy-MM-dd");


    public static Date now() {
        return DateTime.now().toDate();
    }

    public static Date today() {

        return yyyy_MM_ss_dft.parseDateTime(DateTime.now().toString(yyyy_MM_ss_dft)).toDate();
    }

    public static String todayStr() {
        return DateTime.now().toString(yyyy_MM_ss_dft);
    }
}
