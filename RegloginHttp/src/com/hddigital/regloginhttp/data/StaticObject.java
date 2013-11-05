package com.hddigital.regloginhttp.data;

import org.apache.http.cookie.Cookie;

public class StaticObject {
	public static final String domain = "http://192.168.1.9/customer/gambrinus/";
	public static final String checklogin = "http://192.168.1.9/customer/gambrinus/api/me";
	public static final String signin = "http://192.168.1.9/customer/gambrinus/api/signin/";
	public static final String signup = "http://192.168.1.9/customer/gambrinus/api/signup/";
	public static final String captcha = domain + "captcha/signup/";
	public static final String prefName = "mydata";
	public static final String cookieData = "cookie_data";
	public static Cookie mCookie=null;

}
