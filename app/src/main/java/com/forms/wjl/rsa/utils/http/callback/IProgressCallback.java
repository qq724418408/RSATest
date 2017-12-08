package com.forms.wjl.rsa.utils.http.callback;

import java.io.File;

/**
 * description： 下载回调
 * <p/>
 * Created by TIAN FENG on 2017/8/13 20:56
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface IProgressCallback {

    /**
     * 进度
     *
     * @param total   总大小
     * @param current 当前大小
     */
    void onProgress(long total, long current);

    /**
     * 成功
     */
    void onSuccess(File file);

    /**
     * 失败
     */
    void onError(Throwable e);

    /**
     * 一定进入
     */
    void onFinal();
}
