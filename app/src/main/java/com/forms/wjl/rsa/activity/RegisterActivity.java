package com.forms.wjl.rsa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.forms.wjl.rsa.R;
import com.forms.wjl.rsa.utils.test.RSA;
import com.forms.wjl.rsa.utils.http.utils.LogUtils;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;
    private TextView tvEncrypt;
    private TextView tvDecrypt;
    private EditText etUserName;
    private EditText etPwd;
    private EditText etPwd2;
    private Button btnRegister;
    private RSA mRSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRSA = RSA.getInstance();
        initViews();
        initListener();
    }

    private void initListener() {
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initViews() {
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvEncrypt = (TextView) findViewById(R.id.tvEncrypt);
        tvDecrypt = (TextView) findViewById(R.id.tvDecrypt);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPwd = (EditText) findViewById(R.id.etPwd);
        etPwd2 = (EditText) findViewById(R.id.etPwd2);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void register() {
        String encryptPwd = null;
        String decryptPwd = null;
        String userName = etUserName.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String pwd2 = etPwd2.getText().toString().trim();
        InputStream inPublicKey = getResources().openRawResource(R.raw.gumou_rsa_public_key);
        InputStream inPrivateKey = getResources().openRawResource(R.raw.gumou_pkcs8_rsa_private_key);
        byte[] bytes;
        try {
            bytes = mRSA.encryptByPublicKey(pwd.getBytes());
            encryptPwd = RSA.encodeBase64(bytes);
            byte[] decryptByte = RSA.decodeBase64(encryptPwd);
            decryptByte = mRSA.decryptByPrivateKey(decryptByte);
            decryptPwd = new String(decryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String publicKey = mRSA.getPublicKey();
        String privateKey = mRSA.getPrivateKey();
        LogUtils.e("明文：" + pwd);
        LogUtils.e("publicKey：" + publicKey);
        LogUtils.e("privateKey：" + privateKey);
        LogUtils.e("密文：" + encryptPwd);
        LogUtils.e("解密：" + decryptPwd);
        tvEncrypt.setText("密文:\n" + encryptPwd);
        tvDecrypt.setText("解密:\n" + decryptPwd);
    }

}
