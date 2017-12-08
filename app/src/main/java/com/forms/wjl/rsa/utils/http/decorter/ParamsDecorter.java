package com.forms.wjl.rsa.utils.http.decorter;

import android.content.Context;

import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;
import com.forms.wjl.rsa.utils.http.engin.IHttpEngin;

import java.util.Map;

/**
 * description：装饰类 添加公用参数
 * <p/>
 * Created by TIAN FENG on 2017/8/13 21:13
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ParamsDecorter extends IHttpEngin {

    // 在httputils下最好用空的构造器 配合 addDecorter使用
    public ParamsDecorter() {
    }

    // 装饰者直接传对象
    public ParamsDecorter(IHttpEngin engin) {
        super(engin);
    }


    @Override
    public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
        // 直接在这里拦截公共参数的添加
    	intercept(params);
        httpEngin.get(context, url, params, callback);
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
    	// 直接在这里拦截公共参数的添加
    	intercept(params);
        httpEngin.post(context, url, params, callback);
    }

    @Override
    public void downLoad(Context context, String url, Map<String, Object> params, String path, IProgressCallback callback) {
    	// 直接在这里拦截公共参数的添加
    	intercept(params);
    	httpEngin.downLoad(context, url, params,path, callback);
    }

    @Override
    public void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback) {
    	// 直接在这里拦截公共参数的添加
    	intercept(params);
    	httpEngin.upLoad(context, url, params, callback);
    }

    /**
     * 添加共有请求参数
     * @param params
     */
	private void intercept(Map<String, Object> params) {
		// TODO Auto-generated method stub
		//params.put("key", "value");
	}

}
