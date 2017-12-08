package com.forms.wjl.rsa.utils.http.config;

import android.content.Context;

import com.forms.wjl.rsa.bean.PublicKeyBean;
import com.forms.wjl.rsa.utils.http.callback.IHttpCallback;
import com.forms.wjl.rsa.utils.http.utils.HttpUtils;

import java.util.Map;

public class RequestUtil {

    public static void getPublicKey(Context context, String deviceToken, IHttpCallback<PublicKeyBean> callback) {
        HttpUtils.with(context).url(URLConfig.PUBLIC_KEY).addParams("deviceToken", deviceToken).post(callback);
    }

    public static void reqLogin(Context context, String userName, String password, IHttpCallback<String> callback) {
        HttpUtils.with(context).url(URLConfig.LOGIN).addParams("userName", userName).addParams("pwd", password).get(callback);
    }

    public static <T> void reqRegister(Context context, Map<String, Object> params, IHttpCallback<T> callback) {
        HttpUtils.with(context).url(URLConfig.REGISTER).addParams(params).post(callback);
    }

    public static void test(Context context, IHttpCallback<PublicKeyBean> callback) {
        HttpUtils.with(context).url(URLConfig.TEST).post(callback);
    }

}
