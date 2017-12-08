package com.forms.wjl.rsa.utils.http.engin.okhttp2engin;

import android.util.Log;

import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class OkHttp2SuccessRunnable implements Runnable {
	// okhttp 异步请求结果
	private String mResult;
	// 网络请求回调
	private IStringCallaBack mCallback;
	// 链接
	private String mUrl;

	public OkHttp2SuccessRunnable(Response response, IStringCallaBack callback, String url) throws IOException {
		this.mCallback = callback;
		this.mUrl = url;
		mResult = response.body().string();
		//获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
		Headers headers = response.headers();
		String c = headers.get("Set-Cookie");
		OkHttp2Engin.token = c;
		// 后面那一串可以不用,效果一样的
		/*LogUtils.e("info_header---header " + headers);
		LogUtils.e("info_c----session is  :" + c);
		List<String> cookies = headers.values("Set-Cookie");
		if(null == cookies || cookies.size() == 0){
			return;
		}
		String session = cookies.get(0);
		LogUtils.e("info_cookies---onResponse-size: " + cookies);
		String s = session.substring(0, session.indexOf(";"));
		LogUtils.e("info_s----session is  :" + s);*/
	}

	@Override
	public void run() {
		Log.e("43 json = ", mResult);
		try {
			mCallback.onSuccess(mUrl, mResult);
		} catch (Exception e) {
			e.printStackTrace();
			mCallback.onError(mUrl, e);
		} finally {
			mCallback.onFinal(mUrl);
		}
	}
}
