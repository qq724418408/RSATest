package com.forms.wjl.rsa.utils.http.callback;

/**
 * Description : 网络回调避免装饰类在泛型传递过程中解析出问题 所以结果用String固定类型
 *              不做解析
 * Created : TIAN FENG
 * Date : 2017/8/15
 * Email : 27674569@qq.com
 * Version : 1.0
 */

public interface IStringCallaBack {

    /**
     * 成功
     */
    void onSuccess(String url, String result);

    /**
     * 失败
     */
    void onError(String url, Throwable e);

    /**
     * 一定进入
     */
    void onFinal(String url);
}
