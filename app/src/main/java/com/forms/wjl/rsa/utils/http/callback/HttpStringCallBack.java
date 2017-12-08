package com.forms.wjl.rsa.utils.http.callback;

import com.forms.wjl.rsa.utils.http.utils.CallBackSuccessUtils;

/**
 * Description :  HttpUtils网络请求最终以String的json结果回调到这接口类 然根据泛型回调给调用者
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/8/15
 * Email : 27674569@qq.com
 * Version : 1.0
 */


public class HttpStringCallBack<T> implements IStringCallaBack {
    private IHttpCallback<T> mHttpCallback;

    public HttpStringCallBack(IHttpCallback<T> httpCallback) {
        this.mHttpCallback = httpCallback;
    }

    @Override
    public void onSuccess(String url, String result) {
        // 成功并且解析结果回传
        CallBackSuccessUtils.onSuccess(mHttpCallback,result,url);
    }

    @Override
    public void onError(String url, Throwable e) {
    	if (mHttpCallback == null) {
            return;
        }
        mHttpCallback.onError(url, e);
    }

    @Override
    public void onFinal(String url) {
    	if (mHttpCallback == null) {
            return;
        }
        mHttpCallback.onFinal(url);
    }
}
