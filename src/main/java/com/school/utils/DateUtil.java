package com.school.utils;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";


    public static Date string2Date(String strDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }


    public static String date2String(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD);
    }


    public static Date getTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(STANDARD);
        String format = formatter.format(date);
        return string2Date(format);
    }
}
