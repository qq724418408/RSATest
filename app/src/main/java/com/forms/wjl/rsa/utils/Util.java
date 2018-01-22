package com.forms.wjl.rsa.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by bubbly on 2017/12/6.
 */

public class Util {

    /**
     * 手机号码，中间4位星号替换
     *
     * @param phone 手机号
     * @return
     */
    public static String phoneNoHide(String phone) {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 设置输入的内容只能位字母和数字
     * @param edit
     */
    public static void setInputLetterOrDigit(EditText edit) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        edit.setFilters(new InputFilter[]{filter});
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return
     */
    public static String cardIdHide(String cardId) {
        return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
    }

    /**
     * 是否为车牌号（沪A88888）
     */
    public static boolean checkVehicleNo(String vehicleNo) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");
        return pattern.matcher(vehicleNo).find();

    }

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
