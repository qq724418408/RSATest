package com.forms.wjl.rsa.utils.http.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class HttpEnginUtils {
    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 获取接口泛型的实体类型
     */
    public static Class<?> analysisInterfaceInfo(Object object,String callbakName) {
        // 获取接口定义相关泛型
        Type[] genType = object.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = null;//(ParameterizedType) genType[0];
        // 找到对应的interface
        for (int i=0;i< genType.length; i++) {
        	try {
        		parameterizedType = (ParameterizedType) genType[i];
        		// 类名相匹配
                if (parameterizedType.toString().contains(callbakName)) {
    				break;
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }    
        try {
            // 普通泛型类型
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception e) {
            // list类型泛型
            ParameterizedType type = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
            Log.e("type",type.toString());
            // list对象中的泛型 List<String> -> clazz.getName() =  String
            Class clazz = (Class<?>) type.getActualTypeArguments()[0];
            // 拿list (Class<?>) type.getRawType()  -> (Class<?>) type.getRawType().getName() = List
            return clazz;
//            return (Class<?>) type.getRawType();
        }
    }


    /**
     * 获取接口类的实体类型
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
    
    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    public static String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


}
