package com.bdzjn.xml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDate(String value) {
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printDate(Date value) {
        return dateFormat.format(value);
    }
}
