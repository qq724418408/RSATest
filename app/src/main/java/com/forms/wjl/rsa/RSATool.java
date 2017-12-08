package com.forms.wjl.rsa;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class RSATool {
	
	//加密算法RSA
    public static final String KEY_ALGORITHM = "RSA";
    
	//指定公钥存放文件
    private static final String PUBLIC_KEY_FILE = "gumou_rsa_public_key.pem";
    //指定私钥存放文件
    private static final String PRIVATE_KEY_FILE = "gumou_pkcs8_rsa_private_key.pem";
    
    private static String Public_Key = null;
    private static String Private_Key = null;
    
    static{
		Private_Key = getPrivateKey(PRIVATE_KEY_FILE);
		Public_Key = getPublicKey(PUBLIC_KEY_FILE);
    }
    
    /**
     * 获取公钥
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String getPublicKey(String publicKey){
		InputStream is = RSATool.class.getResourceAsStream(publicKey);
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		String readLine = null;
		StringBuffer puplickey = new StringBuffer();
		try {
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					puplickey.append(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return puplickey.toString();
	}
    
    /**
     * 获取私钥
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(String privateKey){  
    	InputStream is = RSATool.class.getResourceAsStream(privateKey);
    	InputStreamReader reader = new InputStreamReader(is);
    	BufferedReader br = new BufferedReader(reader);
		String readLine= null;
		StringBuffer privatekey = new StringBuffer();
		try {
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					privatekey.append(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return privatekey.toString();
    }

	public static String getPublicKey(Context context, int id){
		InputStream is = context.getResources().openRawResource(id);
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		String readLine= null;
		StringBuffer privatekey = new StringBuffer();
		try {
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					privatekey.append(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return privatekey.toString();
	}


	/**
     * 公钥加密 
     * @param source 源数据 
     * @return 
     * @throws Exception 
     */  
    public static String encryptByPublicKey(String source)  
            throws Exception {  
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(Public_Key);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        byte[] content = source.getBytes("utf-8");
        byte[] b = cipher.doFinal(content);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }
    
    /**
     * 私钥解密 
     * @param cryptograph 已加密数据 
     * @return 
     * @throws Exception 
     */  
    public static String decryptByPrivateKey(String cryptograph)  
            throws Exception {  
    	BASE64Decoder decoder = new BASE64Decoder();
    	byte[] encryptedData = decoder.decodeBuffer(cryptograph);
        byte[] keyBytes = decoder.decodeBuffer(Private_Key);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        byte []b = cipher.doFinal(encryptedData);
        return new String(b,"utf-8");
    }
    
	public static void main(String[] args) {
		String content = "123456";			//加密明文
    	try {
    		System.out.println("明文："+content);
    		String cryptograph = encryptByPublicKey(content);	//公钥加密
    		System.out.println("加密后："+cryptograph);
    		String ss = "i+Pr+VRkwZkLT31bAxnVHxwbxZ4GIPmJJDJhY5hN/EUbHlpuBs2WfEmMyGFE9iXKoX9He+56msLP"+
                                                                  "fIVC4rr+cFA8KS1aX/nW3AsKkWVDiHEJpKEPDszV7SQKqsAJC2VfZfkyY4u9AmIGkDE/kWYAGR22"
                                                                  +"Q4Bizq6kXG0ZeNIG0tk=";
    		String decryptedData = decryptByPrivateKey(cryptograph);			//私钥解密
    		String sss = decryptByPrivateKey(ss);			//私钥解密
    		System.out.println("解密后："+decryptedData);
    		System.out.println("解密后："+sss);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
