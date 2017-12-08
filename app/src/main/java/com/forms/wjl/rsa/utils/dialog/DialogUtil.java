package com.forms.wjl.rsa.utils.dialog;

import android.content.Context;

import com.forms.wjl.rsa.R;
import com.forms.wjl.rsa.utils.Util;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

/**
 * Created by bubbly on 2017/12/6.
 */

public class DialogUtil {

    public static TimePickerDialog timePickerDialog (Context context, long currentTimeMillis, OnDateSetListener callBack) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(callBack)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("请选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(Util.string2Date("2000-01-01","yyyy-MM-dd").getTime())
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(currentTimeMillis)
                .setThemeColor(context.getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(context.getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(context.getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
        return mDialogAll;
    }


}
