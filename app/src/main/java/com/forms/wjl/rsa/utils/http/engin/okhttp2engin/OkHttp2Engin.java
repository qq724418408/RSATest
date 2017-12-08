package com.forms.wjl.rsa.utils.http.engin.okhttp2engin;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.forms.wjl.rsa.utils.http.callback.IProgressCallback;
import com.forms.wjl.rsa.utils.http.callback.IStringCallaBack;
import com.forms.wjl.rsa.utils.http.callback.IUpLoadCallback;
import com.forms.wjl.rsa.utils.http.engin.IHttpEngin;
import com.forms.wjl.rsa.utils.http.utils.HttpEnginUtils;
import com.forms.wjl.rsa.utils.http.utils.HttpTimeOuts;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttp2Engin extends IHttpEngin {

    public static String token = "";

    private static OkHttpClient mOkHttpClient;
    private static Handler mHandler = new Handler();

    public OkHttp2Engin() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(HttpTimeOuts.CONNECT_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(HttpTimeOuts.READ_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(HttpTimeOuts.WRITE_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public void get(Context context, final String url, Map<String, Object> params, final IStringCallaBack callback) {
        // 请求路径  参数 + 路径代表唯一标识，存储md5加密url
        final String finalUrl = HttpEnginUtils.jointParams(url, params);
        Log.e("get url：", finalUrl);
        Request.Builder requestBuilder = new Request.Builder().addHeader("cookie", token).url(finalUrl).tag(url);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(url, e);
                        callback.onFinal(url);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                mHandler.post(new OkHttp2SuccessRunnable(response, callback, url));
                Headers headers = response.headers();
                String c = headers.get("Set-Cookie");
                OkHttp2Engin.token = c;
            }
        });
    }

    @Override
    public void post(Context context, final String url, Map<String, Object> params, final IStringCallaBack callback) {
    	// 拼接请求头
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .addHeader("cookie", token)
                .url(url)
                .tag(url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request arg0, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(url, e);
                        callback.onFinal(url);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                mHandler.post(new OkHttp2SuccessRunnable(response, callback, url));
            }
        });
    }

    @Override
    public void downLoad(Context context, String url, Map<String, Object> params, final String fileName, final IProgressCallback callback) {
    	// 储存下载文件的目录
        String savePath = null;
		try {
			savePath = HttpEnginUtils.isExistDir(fileName.substring(0, fileName.lastIndexOf("/")));
		} catch (IOException e1) {
			callback.onError(e1);
			callback.onFinal();
		}
        // 创建文件
        final File file = new File(savePath, fileName.substring(fileName.lastIndexOf("/")));
        // 文件的大小
        final long fileSize = file.length();
        // 下载为get请求 拼接url
        final String finalUrl = HttpEnginUtils.jointParams(url, params);
        final Request request = new Request.Builder()
                .addHeader("cookie", token)
                .url(finalUrl)
                .header("RANGE", "bytes=" + fileSize + "-")// 已经下载位置开始
                .tag(url)
                .build();
        mOkHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request call, final IOException e) {
                        // 下载失败
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(e);
                                callback.onFinal();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        InputStream is = null;
                        // 每次读100k
                        byte[] buf = new byte[1024 * 100];
                        int len;
                        FileOutputStream fos = null;
                        try {
                            // 获取流
                            is = response.body().byteStream();
                            // 获取总大小 = 当前剩余大小 + 文件已经下载的大小
                            final long total = response.body().contentLength() + fileSize;
                            // 文件输出流，true 可以接着写实现断点续传
                            fos = new FileOutputStream(file, true);
                            // 起始位置为文件已经下载的大小
                            long sum = fileSize;
                            // 循环写
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                fos.flush();
                                sum += len;
                                // 回调进度
                                final long finalSum = sum;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onProgress(total, finalSum);
                                    }
                                });
                            }
                            // 成功后返回
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(file);
                                    callback.onFinal();
                                }
                            });
                        } catch (final Exception e) {
                            // 失败
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError(e);
                                    callback.onFinal();
                                }
                            });

                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void upLoad(Context context, String url, Map<String, Object> params, final IUpLoadCallback callback) {
        RequestBody requestBody = appendBody(params);
        final Request request = new Request.Builder()
                .addHeader("cookie", token)
                .url(url)
                .tag(url)
                .post(new OkHttp2ProgressRequestBody(requestBody, callback))// 装饰requestbody进行上传进度监听
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                        callback.onFinal();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(result);
                        callback.onFinal();
                    }
                });
            }
        });
    }

    /**
     * 组装post请求参数body
     */

    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBuilder builder = new MultipartBuilder();
        if (params!=null&&params.size()>0) {
        	builder.type(MultipartBuilder.FORM);
            addParams(builder, params);
            return builder.build();
		}
        return RequestBody.create(null, "");
    }

    // 添加参数
    private void addParams(MultipartBuilder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
//                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 取消tag下的所有请求
     *
     * @param url tag
     */
    @Override
	public void cancle(String url) {
        mOkHttpClient.cancel(url);
    }

}
