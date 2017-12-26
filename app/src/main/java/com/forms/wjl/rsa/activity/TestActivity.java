package com.forms.wjl.rsa.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.forms.wjl.rsa.R;
import com.forms.wjl.rsa.utils.TimeUtils;
import com.forms.wjl.rsa.utils.Util;
import com.forms.wjl.rsa.utils.dialog.DialogUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.komi.slider.ISlider;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderUtils;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSelectTime;
    private TextView tvChangeResult;
    private TextView tvResult;
    private EditText etSecond;
    private Button btnChange;
    private Button btnSecondChange;
    private long currentTimeMillis;
    private String selectTime;
    private ISlider iSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
        initListener();
        currentTimeMillis = System.currentTimeMillis();
        SliderConfig mConfig = new  SliderConfig.Builder()
                .secondaryColor(Color.TRANSPARENT)
                .edge(false)
                .build();
        iSlider = SliderUtils.attachActivity(this, mConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initListener() {
        tvSelectTime.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnSecondChange.setOnClickListener(this);
    }

    private void initViews() {
        tvSelectTime = (TextView) findViewById(R.id.tvSelectTime);
        tvChangeResult = (TextView) findViewById(R.id.tvChangeResult);
        tvResult = (TextView) findViewById(R.id.tvResult);
        etSecond = (EditText) findViewById(R.id.etSecond);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnSecondChange = (Button) findViewById(R.id.btnSecondChange);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSelectTime:
                DialogUtil.timePickerDialog(this, currentTimeMillis, new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        selectTime = Util.date2String(Util.long2Date(millseconds), "yyyy-MM-dd HH:mm:ss");
                        tvSelectTime.setText(selectTime);
                        currentTimeMillis = millseconds;
                    }
                }).show(getSupportFragmentManager(), "all");
                break;
            case R.id.btnChange:
                if (null != selectTime) {
                    TimeUtils timeUtils = TimeUtils.timeSpanToNow(selectTime);
                    tvChangeResult.setText("在" + timeUtils.mSpanDayText + "" + timeUtils.mSpanHourText + "" + timeUtils.mSpanMinuteText + timeUtils.mSpanSecondText + "之前");
                }
                break;
            case R.id.btnSecondChange:
                String second = etSecond.getText().toString().trim();
                if (null != second) {
                    TimeUtils timeUtils = TimeUtils.timeSpanSecond(Long.parseLong(second));
                    tvResult.setText("在" + timeUtils.mSpanDayText + "" + timeUtils.mSpanHourText + "" + timeUtils.mSpanMinuteText + timeUtils.mSpanSecondText + "之前");
                }
                break;
            default:
                break;
        }
    }
}
