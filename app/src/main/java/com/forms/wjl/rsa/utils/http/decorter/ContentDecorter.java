package com.forms.wjl.rsa.utils.http.decorter;

import android.content.Context;

import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;
import com.forms.wjl.rsa.utils.http.engin.IHttpEngin;
import com.forms.wjl.rsa.utils.http.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.Map;

public class ContentDecorter extends IHttpEngin {

	public ContentDecorter() {

	}

	public ContentDecorter(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		httpEngin.get(context, url, params, new ContentCallback(callback));
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		httpEngin.post(context, url, params, new ContentCallback(callback));
	}

	@Override
	public void downLoad(Context context, String url, Map<String, Object> params, String path,
			IProgressCallback callback) {
		httpEngin.downLoad(context, url, params, path, callback);
	}

	@Override
	public void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback) {
		httpEngin.upLoad(context, url, params,
				/* new ContentIUpLoadCallback(callback) */callback);
	}

	private class ContentCallback implements IStringCallaBack {
		private IStringCallaBack callback;

		public ContentCallback(IStringCallaBack callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onSuccess(String url, String result) {
			LogUtils.e("json = "+result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getString("rtnCode").equals("10000")) {
					result = jsonObject.getString("content");
					callback.onSuccess(url, result);
				} else {
					callback.onError(url, new ConnectException(result));
				}

			} catch (Throwable e) {
				e.printStackTrace();
				LogUtils.e(e.getMessage());
				callback.onError(url, e);
			}
		}

		@Override
		public void onError(String url, Throwable e) {
			callback.onError(url, e);
		}

		@Override
		public void onFinal(String url) {
			callback.onFinal(url);
		}

	}

	private class ContentIUpLoadCallback implements IUpLoadCallback {

		private IUpLoadCallback callback;
		

		public ContentIUpLoadCallback(IUpLoadCallback callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onProgress(long total, long current) {
			callback.onProgress(total, current);
		}

		@Override
		public void onError(Throwable e) {
			callback.onError(e);
		}

		@Override
		public void onFinal() {
			callback.onFinal();
		}
		@Override
		public void onSuccess(String result) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getString("rtnCode").equals("10000")) {
					result = jsonObject.getString("content");
					callback.onSuccess(result);
				} else {
					callback.onError(new ConnectException(result));
				}

			} catch (JSONException e) {
				e.printStackTrace();
				callback.onError(e);
			}
		}

	}
}
