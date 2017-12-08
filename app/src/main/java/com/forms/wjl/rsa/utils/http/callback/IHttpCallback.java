package com.forms.wjl.rsa.utils.http.callback;

/**
 * description：普通回调
 * <p/>
 * Created by TIAN FENG on 2017/8/13 20:44
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface IHttpCallback<T> {

    /**
     * 成功 url：多个请求实现统一接口时，做判断标识符
     */
    void onSuccess(String url, T result);

    /**
     * 失败 url：多个请求实现统一接口时，做判断标识符
     */
    void onError(String url, Throwable e);

    /**
     * 一定进入 url：多个请求实现统一接口时，做判断标识符
     */
    void onFinal(String url);
}
