package com.forms.wjl.rsa.utils.test;

import android.util.Base64;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 调用范例:
 *          初始化：
 *          mRSA = RSA.getInstance();
 *          公钥加密：
 *          byte[] bytes = mRSA.encryptByPublicKey(content.getBytes());
 *          String encrypt = RSA.encodeBase64(bytes);
 *          Log.e(TAG, "密文-->" + encrypt);
 *          私钥解密：
 *          byte[] decryptByte = RSA.decodeBase64(encryptContent);
 *          decryptByte = mRSA.decryptByPrivateKey(decryptByte);
 *          Log.e(TAG, "明文-->" + new String(decryptByte));
 *          查看生成的公钥私钥：
 *          Log.e(TAG, "PublicKey-->" + mRSA.getPublicKey());
 *          Log.e(TAG, "PrivateKey-->" + mRSA.getPrivateKey());
 */
public class RSA {
    /**
     * KEY_ALGORITHM
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 加密Key的长度等于1024
     */
    private static int KEYSIZE = 1024;
    /**
     * 解密时必须按照此分组解密
     */
    private static int decodeLen = KEYSIZE / 8;
    /**
     * 加密时小于117即可
     */
    public static int encodeLen = 110;//(DEFAULT_KEY_SIZE / 8) - 11;

    private static final String PUBLIC_KEY = "publicKey";

    private static final String PRIVATE_KEY = "privateKey";

    private static final String MODULES = "RSAModules";

    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    private static final RSA ourInstance = new RSA();
    private String publicKey;
    private String privateKey;

    static {

    }

    public static RSA getInstance() {
        return ourInstance;
    }

    private RSA() {
        generateKey();
    }

    public void generateKey() {
        Map<String, Object> map = createKeyPair();
        this.privateKey = getPrivateKey(map);
        this.publicKey = getPublicKey(map);
    }

    private Map<String, Object> createKeyPair() {
        Map<String, Object> keys = new HashMap<>(3);
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEYSIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            BigInteger modules = privateKey.getModulus();
            keys.put(PUBLIC_KEY, publicKey);
            keys.put(PRIVATE_KEY, privateKey);
            keys.put(MODULES, modules);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keys;
    }

    private String getPrivateKey(Map<String, Object> keys) {
        Key key = (Key) keys.get(PRIVATE_KEY);
        return encodeBase64(key.getEncoded());
    }

    private String getPublicKey(Map<String, Object> keys) {
        Key key = (Key) keys.get(PUBLIC_KEY);
        return encodeBase64(key.getEncoded());
    }

    public static byte[] decodeBase64(String base64) {
        return Base64.decode(base64, Base64.DEFAULT);
    }

    public static String encodeBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /*=============================*/
    /*===========公钥加密==========*/
    /*=============================*/

    public byte[] encryptByPublicKey(byte[] data) throws Exception {
        return encryptByPublicKey(data, publicKey);
    }

    public byte[] encryptByPublicKey(byte[] data, String key){
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input encryption data is empty");
        }
        byte[] encode = new byte[]{};
        for (int i = 0; i < data.length; i += encodeLen) {
            byte[] subArray = ArrayUtils.subarray(data, i, i + encodeLen);
            byte[] doFinal = encryptPublic(subArray, key);
            encode = ArrayUtils.addAll(encode, doFinal);
        }
        return encode;
    }

    private byte[] encryptPublic(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(key));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
           throw new IllegalArgumentException("无此算法");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("BadPadding");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("IllegalBlockSize");
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("无此Padding");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("公钥非法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("公钥数据为空");
        }
    }

    private PublicKey loadPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("公钥非法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("公钥数据为空");
        }
    }

    /*=============================*/
    /*===========私钥解密==========*/
    /*=============================*/

    public byte[] decryptByPrivateKey(byte[] data) {
        return decryptByPrivateKey(data, privateKey);
    }

    public byte[] decryptByPrivateKey(byte[] data, String key) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null");
        }
        byte[] buffers = new byte[]{};
        for (int i = 0; i < data.length; i += decodeLen) {
            byte[] subArray = ArrayUtils.subarray(data, i, i + decodeLen);
            byte[] doFinal = decryptPrivate(subArray, key);
            buffers = ArrayUtils.addAll(buffers, doFinal);
        }
        return buffers;
    }

    /**
     * 私钥解密
     * @param content
     * @param privateKey
     * @return
     */
    public String decryptByPrivateKey(String content, String privateKey) {
        if (content == null) {
            throw new IllegalArgumentException("Input data is null");
        }
        byte[] data = content.getBytes();
        byte[] buffers = new byte[]{};
        for (int i = 0; i < data.length; i += decodeLen) {
            byte[] subArray = ArrayUtils.subarray(data, i, i + decodeLen);
            byte[] doFinal = decryptPrivate(subArray, privateKey);
            buffers = ArrayUtils.addAll(buffers, doFinal);
        }
        return new String(buffers);
    }

    private byte[] decryptPrivate(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(key));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此算法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("私钥数据为空");
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("无此算法padding");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("私钥非法");
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("IllegalBlockSize");
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("BadPadding");
        }
    }

    private PrivateKey loadPrivateKey(String privateKeyStr) {
        try {
            byte[] buffer = decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("私钥非法");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("私钥数据为空");
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
