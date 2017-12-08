package com.forms.wjl.rsa.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bubbly on 2017/12/6.
 */

public class Util {

    public static Date long2Date (long millseconds) {
        Date date = new Date(millseconds);
       return date;
    }

    public static Date string2Date (String date, String reg) {
        Date dt = new Date();
        try {
            dt = new SimpleDateFormat(reg).parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt;
    }

    public static long date2Long (Date date) {
       return date.getTime();
    }

    public static String date2String (Date date, String reg) {
        DateFormat df = new SimpleDateFormat(reg);
        String format = df.format(date);
        return format;
    }
}
