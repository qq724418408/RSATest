package com.forms.wjl.rsa.utils.http.utils;

import android.content.Context;
import android.text.TextUtils;

import com.forms.wjl.rsa.utils.http.callback.HttpStringCallBack;
import com.forms.wjl.rsa.utils.http.callback.IHttpCallback;
import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;
import com.forms.wjl.rsa.utils.http.engin.IHttpEngin;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    /**
     * 伪Builder模式 链式调用
     * 参数相当于params
     */
    // 上下文 后面dialog用
    private Context mContext;
    // 接口
    private String mUrl;
    // 请求参数
    private Map<String, Object> mParams;
    // path
    private String mPath;
    // 请求引擎
    private IHttpEngin mHttpEngin;
    // 装饰引擎保存类避免重复装饰同一个装饰类
    private Map<String, IHttpEngin> mDecortorHttpEngins;

    // 全局网络请求引擎
    private static IHttpEngin mEngin;
    // get 请求
    private static final int GET = 0x0001;
    // post 请求
    private static final int POST = 0x0011;

    /***
     * 初始化
     */
    private HttpUtils(Context context) {
        this.mContext = context;
        // 保证调用引擎是全局引擎
        mHttpEngin = mEngin;
        mParams = new HashMap<>();
        mDecortorHttpEngins = new HashMap<>();
    }

    /**
     * 初始化引擎 application 配置使用
     */
    public static void initHttpEngin(IHttpEngin engin) {
        mEngin = engin;
    }

    /**
     * 网络工具入口
     */
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    /**
     * 添加不一样的装饰引擎
     */
    public HttpUtils addDecortor(IHttpEngin engin) {
    	if(engin==null)
    		return this;
        // 设置引擎装饰着 并且避免重复装饰
        if (mHttpEngin != null && mDecortorHttpEngins.get(engin.getClass().getName()) == null) {
            // 加入集合
            mDecortorHttpEngins.put(engin.getClass().getName(), engin);
            // 设置装饰引擎
            engin.addDecortorEngin(mHttpEngin);
            mHttpEngin = engin;
        }
        // 防止未注册通过设置调用
        if (mHttpEngin == null) {
            mHttpEngin = engin;
        }
        return this;
    }

    /**
     * 请求接口
     *
     * @param url
     */
    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     * 下载路径
     *
     * @param filePath 文件下载后的路径，需包含文件名
     */
    public HttpUtils path(String filePath) {
        this.mPath = filePath;
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public HttpUtils addParams(String key, Object value) {
    	if (!TextUtils.isEmpty(key)&&value!=null) {
    		 mParams.put(key, value);
		}
        return this;
    }

    /**
     * 添加请求参数
     */
    public HttpUtils addParams(Map<String, Object> params) {
    	if (params != null) {
    		mParams.putAll(params);
		}
        return this;
    }


    /**
     * get 不需要回调
     */
    public void get() {
        get(null);
    }

    /**
     * get
     */
    public <T> void get(IHttpCallback<T> callback) {
        execute(callback, GET);
    }

    /**
     * post 不需要回调
     */
    public void post() {
        post(null);
    }

    /**
     * post
     */
    public <T> void post(IHttpCallback<T> callback) {
        execute(callback, POST);
    }

    /**
     * 下载
     */
    public void downLoad(IProgressCallback callback) {
        checkUrl();
        if (TextUtils.isEmpty(mPath) || !mPath.contains(".")) { // 判断是否是文件的合法路径，判断不完美
            throw new IllegalStateException("path must be file absolute path,cannot folder.");
        }
        mHttpEngin.downLoad(mContext, mUrl, mParams, mPath, callback);
    }

    /**
     * 上传
     */
    public void upLoad(IUpLoadCallback callback) {
        checkUrl();
        mHttpEngin.upLoad(mContext, mUrl, mParams, callback);
    }

    /**
     * 执行
     */
    private <T> void execute(final IHttpCallback<T> callback, int methodType) {
        checkUrl();
        // 执行后不会进入装饰的方法顾将装饰管理清空
        mDecortorHttpEngins = null;
        switch (methodType) {
            case GET:
                mHttpEngin.get(mContext, mUrl, mParams, new HttpStringCallBack<>(callback));
                break;
            case POST:
                mHttpEngin.post(mContext, mUrl, mParams, new HttpStringCallBack<>(callback));
                break;
        }
    }

    /**
     * 取消请求
     */
    public static void cancle(String url) {
        mEngin.cancle(url);
    }

    // 检测url
    private void checkUrl() {
        if (TextUtils.isEmpty(mUrl)) {
            throw new NullPointerException("url is null");
        }
    }

}
