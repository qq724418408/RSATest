package com.forms.wjl.rsa.utils.http.decorter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;
import com.forms.wjl.rsa.utils.http.engin.IHttpEngin;
import com.forms.wjl.rsa.utils.http.utils.HttpUtils;

import java.util.Map;

/**
 * Description : dialog 装饰 dismiss后请滞空避免内存泄露
 * <p/>
 * Created : TIAN FENG Date : 2017/8/14 Email : 27674569@qq.com Version : 1.0
 */

public class DialogDecorter extends IHttpEngin {

	private Dialog mDialog;

	public DialogDecorter() {
	}

	public DialogDecorter(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		creatDialog(context,url);
		httpEngin.get(context, url, params, new DialogEnginCallBack(callback));
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		// 创建dialog并且显示
		creatDialog(context,url);
		httpEngin.post(context, url, params, new DialogEnginCallBack(callback));
	}

	@Override
	public void downLoad(Context context, String url, Map<String, Object> params, String path,
			IProgressCallback callback) {
		creatDialog(context,url);
		httpEngin.downLoad(context, url, params, path, callback);
	}

	@Override
	public void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback) {
		creatDialog(context,url);
		httpEngin.upLoad(context, url, params, callback);
	}

	/**
	 * 根据情况创建dialog
	 * 
	 * @param context
	 */
	private void creatDialog(Context context,final String url) {
		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					dialog.dismiss();
					HttpUtils.cancle(url);
					return true;
				}
				return false;
			}
		});
	}

	public class DialogEnginCallBack implements IStringCallaBack {

		private IStringCallaBack mCallBack;

		public DialogEnginCallBack(IStringCallaBack callback) {
			this.mCallBack = callback;
		}

		/**
		 * 成功
		 */
		@Override
		public void onSuccess(String url, String result) {
			mCallBack.onSuccess(url, result);
		}

		/**
		 * 失败
		 */
		@Override
		public void onError(String url, Throwable e) {
			mCallBack.onError(url, e);
		}

		/**
		 * 一定进入
		 */
		@Override
		public void onFinal(String url) {
			mCallBack.onFinal(url);
			if (mDialog != null) {
				mDialog.dismiss();
			}
		}
	}
}
