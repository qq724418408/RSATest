package com.forms.wjl.rsa.utils.http.engin;

import android.content.Context;

import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;

import java.util.Map;

public abstract class IHttpEngin {

    protected IHttpEngin httpEngin;

    public IHttpEngin() {

    }

    /**
     * 处理装饰对象
     * @param engin
     */
    public IHttpEngin(IHttpEngin engin) {
        addDecortorEngin(engin);
    }

    /**
     * 装饰时提供空构造函数时使用
     */
    public void addDecortorEngin(IHttpEngin engin) {
        if (httpEngin != null) {
            engin.addDecortorEngin(httpEngin);
        }
        httpEngin = engin;
    }

    /**
     * 取消请求 被装饰引擎需重写
     */
    public void cancle(String url){
    	httpEngin.cancle(url);
    };

    public abstract void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback);

    public abstract void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback);

    public abstract void downLoad(Context context, String url, Map<String, Object> params, String fileName, IProgressCallback callback);

    public abstract void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback);

}
