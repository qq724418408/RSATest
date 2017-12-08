package com.forms.wjl.rsa.utils.http.config;

public class URLConfig {

    private static final String IP = "http://192.168.0.50:8080";
//    private static final String ROOT_PATH = "/RsaPro";
    private static final String ROOT_PATH = "/NewRsaPro";
    private static final String SERVER_PATH = IP + ROOT_PATH;

    public static final String TEST = "http://192.168.0.132:8080/test.resp";

    public static final String PUBLIC_KEY = SERVER_PATH + "/rsaPreLogin.do";
    public static final String LOGIN = SERVER_PATH + "/rsaLogin.do";
    public static final String REGISTER = SERVER_PATH + "";

}
