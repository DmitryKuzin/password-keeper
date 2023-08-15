package com.passwordkeeper.utils;

import org.apache.logging.log4j.core.util.datetime.FastDateFormat;

import java.util.Date;

public class DateUtils {
    public static FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd.MM.yyyy HH:mm");

    public static String dateToString(Date date) {
        return fastDateFormat.format(date);
    }
}
