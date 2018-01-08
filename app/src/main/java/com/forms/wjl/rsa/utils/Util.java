package com.forms.wjl.rsa.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

    /**
     * 根据输入的数字显示金额格式
     *
     * @return
     */
    public static String moneyFormat(String money) {
        if (TextUtils.isEmpty(money)) {
            return "0.00";
        } else if ("0".equals(money) || "0.00".equals(money)) {
            return "0.00";
        } else {
            money = money.replaceAll(",", "").replaceAll("￥", "");
            double d = Double.parseDouble(money);
            if (d < 1) {
                return String.valueOf(d);
            }
            DecimalFormat df = new DecimalFormat("#,###.00");
            String m = df.format(d);
            return m;
        }
    }

    /**
     * 根据输入的数字显示金额格式，前面加￥
     *
     * @return
     */
    public static String money$Format(String money) {
        if (TextUtils.isEmpty(money)) {
            return "0.00"; // m³㎡
        } else if ("0".equals(money) || "0.00".equals(money)) {
            return "¥0.00";
        } else {
            money = money.replaceAll(",", "").replaceAll("¥", "");
            double d = Double.parseDouble(money);
            if (d < 1) {
                return "¥" + String.valueOf(d);
            }
            DecimalFormat df = new DecimalFormat("#,###.00");
            String m = df.format(d);
            return "¥" + m;
        }
    }


}
