package com.forms.wjl.rsa.base;

import android.app.Application;

import com.forms.wjl.rsa.utils.http.decorter.ContentDecorter;
import com.forms.wjl.rsa.utils.http.engin.okhttp2engin.OkHttp2Engin;
import com.forms.wjl.rsa.utils.http.utils.HttpUtils;
import com.tencent.bugly.Bugly;

public class BaseApplication extends Application {

	private static BaseApplication baseApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		baseApplication = this;/**
		 * 调用okhttp2进行网络请求
		 * 原项目已经包含了post，get请求，如不需要可以将，post和get注释掉
		 * 上传下载包含进度显示
		 * 如后续需要升级或替换网络库请实现IHttpEngin接口
		 * 将new OkHttp2Engin() 替换为对应封装类，其他调用部分逻辑不变
		 * 如升级okhttp3请将okhttp3代码注释部分放开new OkHttp2Engin()改为
		 * new OkHttp3Engin()，并将okhttp3的jar包或者依赖库配置成功
		 */
		HttpUtils.initHttpEngin(new ContentDecorter(new OkHttp2Engin()));
		Bugly.init(baseApplication, "1400008376", false);
	}

}
