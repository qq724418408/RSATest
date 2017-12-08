package com.forms.wjl.rsa.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

    private static final String sRSATransformation = "RSA/ECB/PKCS1Padding";

    /**
     * sha1加密
     *
     * @param text 目标文本
     * @return 对目标文本使用sha1算法加密后得到的文本
     */
    public static String sha1(String text) {
        String encryptText = null;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(text.getBytes());
            encryptText = bytes2HexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    public static String getPublicKey() {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPublicKey publicKeyRSA = (RSAPublicKey) keyPair.getPublic();
        byte[] encoded = publicKeyRSA.getEncoded();
        byte[] publicKey = Base64.encode(encoded, Base64.DEFAULT);
        return new String(publicKey);
    }

    public static String gePrivateKey() {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPrivateKey privateKeyRSA = (RSAPrivateKey) keyPair.getPrivate();
        byte[] privateKey = Base64.encode(privateKeyRSA.getEncoded(), Base64.DEFAULT);
        return new String(privateKey);
    }

    /**
     * 公钥解密
     *
     * @param text      待解密数据
     * @param publicKey 密钥
     * @return 解密数据
     */
    public static String decryptByPublicKey(String text, String publicKey) {
        String decryptStr = null;
        try {
            byte[] publicKeyByte = publicKey.getBytes();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(sRSATransformation);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);
            decryptStr = new String(cipher.doFinal(base64Decrypt(text.getBytes())));
            return decryptStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    /**
     * 公钥对字符串进行加密
     *
     * @param text 原文
     * @return 加密数据
     */
    public static String encryptByPublicKey(String text, byte[] publicKey) {
        String encryptStr = null;
        try {
            byte[] decode = Base64.decode(publicKey, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cp = Cipher.getInstance(sRSATransformation);
            cp.init(Cipher.ENCRYPT_MODE, keyPublic);
            byte[] doFinal = cp.doFinal(text.getBytes());
            encryptStr = new String(base64Encrypt(doFinal));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * 私钥解密
     *
     * @param text       待解密数据
     * @param privateKey 密钥
     * @return 解密数据
     */
    public static String decryptByPrivateKey(String text, byte[] privateKey) {
        String decryptStr = null;
        try {
            byte[] decode = Base64.decode(privateKey, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(sRSATransformation);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            decryptStr = new String(cipher.doFinal(base64Decrypt(text.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    /**
     * 私钥加密
     *
     * @param text       待加密数据
     * @param privateKey 密钥
     * @return 加密数据
     */
    public static String encryptByPrivateKey(String text, byte[] privateKey) {
        String encryptStr = null;
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(sRSATransformation);
            cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
            encryptStr = new String(base64Encrypt(cipher.doFinal(text.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    public static byte[] getPublicKeyByKeyPair() {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPublicKey publicKeyRSA = (RSAPublicKey) keyPair.getPublic();
        byte[] encoded = publicKeyRSA.getEncoded();
        byte[] publicKey = Base64.encode(encoded, Base64.DEFAULT);
        return publicKey;
    }

    public static byte[] gePrivateKeyByKeyPair() {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPrivateKey privateKeyRSA = (RSAPrivateKey) keyPair.getPrivate();
        byte[] privateKey = Base64.encode(privateKeyRSA.getEncoded(), Base64.DEFAULT);
        return privateKey;
    }

    //##############################################################################################

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    /**
     * 字符转字节
     *
     * @param hexChar
     * @return
     */
    private static short charToByte(char hexChar) {
        return (byte) "0123456789abcdef".indexOf(hexChar);
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToHexStr(byte[] bytes) {
        char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int length = bytes.length;
        char c[] = new char[length * 2];
        int k = 0;
        for (int i = 0; i < length; i++) {
            byte b = bytes[i];
            c[k++] = hex[b >>> 4 & 0xf];
            c[k++] = hex[b & 0xf];
        }
        return new String(c);
    }

    /**
     * base64加密
     *
     * @param text 加密内容
     * @return 加密后的内容
     */
    public static byte[] base64Encrypt(byte[] text) {
        return Base64.encode(text, Base64.DEFAULT);
    }

    /**
     * base64解密
     *
     * @param text 解密内容
     * @return 解密后得到的内容
     */
    public static byte[] base64Decrypt(byte[] text) {
        return Base64.decode(text, Base64.DEFAULT);
    }

    /**
     * 随机生成RSA密钥对
     * 使用：
     * 公钥
     * RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
     * 私钥
     * RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
     *
     * @return 随机生成的密钥对
     */
    public static KeyPair generateRSAKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            keyPair = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }
}
