package com.forms.wjl.rsa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forms.wjl.rsa.R;
import com.forms.wjl.rsa.bean.PublicKeyBean;
import com.forms.wjl.rsa.utils.RSA;
import com.forms.wjl.rsa.utils.Util;
import com.forms.wjl.rsa.utils.dialog.DialogUtil;
import com.forms.wjl.rsa.utils.http.callback.IHttpCallback;
import com.forms.wjl.rsa.utils.http.config.RequestUtil;
import com.forms.wjl.rsa.utils.http.utils.LogUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.komi.slider.ISlider;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderUtils;
import com.tencent.bugly.beta.Beta;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister;
    private TextView tvTime;
    private TextView tvResult;
    private EditText etUserName;
    private EditText etPwd;
    private Button btnLogin;
    private long currentTimeMillis;
    private Intent intent;
    private ISlider iSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Beta.checkUpgrade();
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
        intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            if (null != intent) {
                Uri uri = intent.getData();
                if (null != uri) {
                    String a = uri.getQueryParameter("a");
                    LogUtils.e("--------" + a);
                } else {
                    LogUtils.e("--------uri is null");
                }
            } else {
                LogUtils.e("--------intent is null");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initListener() {
        tvRegister.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void initViews() {
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvResult = (TextView) findViewById(R.id.tvResult);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void login(String userName, String pwd) {
        RequestUtil.reqLogin(this, userName, pwd, new IHttpCallback<String>() {
            @Override
            public void onSuccess(String url, String result) {
                tvResult.setText("登录成功");
                LogUtils.e(url);
            }

            @Override
            public void onError(String url, Throwable e) {
                LogUtils.e(e.getMessage());
                //tvResult.setText("登录失败："+ e.getMessage());
            }

            @Override
            public void onFinal(String url) {
                LogUtils.e("url：" + url);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Intent intent = new Intent();
                try {
                    intent.setData(Uri.parse("upwallet://"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "未安装", Toast.LENGTH_SHORT).show();
                    intent.setData(Uri.parse("http://www.baidu.com/"));
                    startActivity(intent);
                }

//                if (TextUtils.isEmpty(etPwd.getText())) {
//                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(etUserName.getText())) {
//                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                getPublicKey();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, TestActivity.class));
                finish();
                break;
            case R.id.tvTime:
                DialogUtil.timePickerDialog(this, currentTimeMillis, new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date2String = Util.date2String(Util.long2Date(millseconds), "yyyy-MM-dd HH:mm:ss");
                        tvResult.setText(date2String);
                        currentTimeMillis = millseconds;
                    }
                }).show(getSupportFragmentManager(), "all");
                break;
        }
    }

    private void getPublicKey() {
        RequestUtil.getPublicKey(this, null, new IHttpCallback<PublicKeyBean>() {
            @Override
            public void onSuccess(String url, PublicKeyBean result) {
                LogUtils.e("url：" + url);
                String publicKey = result.getPublicKey();
                //String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmxaKq18+Yp01KRqRtoKy2AbOO8ky\nL5SYfaQ05sug0bViKv92cpL5lant8KiGnDKh9+uRD+TXBK3Wz75BDSV6/LGDJKFa7/viWYHcgYEot/0GAdgyGXK1iE4FGOSASyNcYeed7W1Pte5v5sQLCwQi656vETDNeTRawviZfPrRrGQIDAQAB";
                String userName = etUserName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                String encryptPwd = null;
                try {
                    encryptPwd = RSA.encryptByPublicKey(pwd, publicKey.getBytes());
                    login(userName, URLEncoder.encode(encryptPwd));
                    LogUtils.e("明文：" + pwd);
                    LogUtils.e("密文：" + encryptPwd);
                } catch (Exception e) {
                    LogUtils.e(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String url, Throwable e) {
                LogUtils.e(e.getMessage());
                LogUtils.e("url：" + url);
            }

            @Override
            public void onFinal(String url) {
                LogUtils.e("url：" + url);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        intent = null;
        iSlider.slideExit();
    }
}
