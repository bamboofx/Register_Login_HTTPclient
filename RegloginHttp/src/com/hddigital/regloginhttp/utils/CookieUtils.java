package com.hddigital.regloginhttp.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import com.hddigital.regloginhttp.data.StaticObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class CookieUtils {
	public static void setCookie(Context mContext, Cookie cookie) {
		SharedPreferences pre = mContext.getSharedPreferences(StaticObject.cookieData, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString("name", cookie.getName());
		editor.putString("value", cookie.getValue());
		editor.putString("expirity", cookie.getExpiryDate().toString());
		editor.putString("domain", cookie.getDomain());
		editor.commit();
	}

	@SuppressLint("SimpleDateFormat")
	public static BasicClientCookie getCookie(Context mContext) {
		
		SharedPreferences pref = mContext.getSharedPreferences(StaticObject.cookieData, Context.MODE_PRIVATE);
		BasicClientCookie cookie = new BasicClientCookie(pref.getString("name", ""),pref.getString("value", ""));
		pref.getString("name", "");
		pref.getString("value", "");
		String expirity=pref.getString("expirity", "");
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date dateExp=null;
		try {  
		     dateExp= format.parse(expirity); 
		
		}catch(Exception e) {
			System.out.print(e.toString());
		}
		String domain=pref.getString("domain", "");
		cookie.setDomain(domain);
		if (dateExp!=null) {
			cookie.setExpiryDate(dateExp);
		}
		return cookie;
	}
}
