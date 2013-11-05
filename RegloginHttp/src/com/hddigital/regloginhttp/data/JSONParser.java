package com.hddigital.regloginhttp.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import static com.hddigital.regloginhttp.utils.LogUtils.*;

public class JSONParser implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	static DefaultHttpClient httpClient;

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {

		if (httpClient == null)
			httpClient = new DefaultHttpClient();

		/*if (cookieStore == null)
			cookieStore = new BasicCookieStore();

		// Create local HTTP context

		if (localContext == null)
			localContext = new BasicHttpContext();
		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);*/

		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Cookie> mCookies = null;
		mCookies = httpClient.getCookieStore().getCookies();

		if (mCookies.isEmpty()) {
			log("test_runner: " + "Cookies: None");
		} else {
			for (int i = 0; i < mCookies.size(); i++) {
				log("test_runner: " + "Cookies: [" + i + "]" + mCookies.get(i).toString());

			}
		}
		CookieStore cookieStore = new BasicCookieStore();

		for (Cookie cook : mCookies) {
			if (cook.getName().equals("PHPSESSID")) {
				User.setSession(cook.toString());
				StaticObject.mCookie = cook;				
			}
			cookieStore.addCookie(cook);
		}
		/*if (mCookies.size()==0) {
			if(StaticObject.mCookie!=null)
				cookieStore.addCookie(StaticObject.mCookie);
		}*/
		httpClient.setCookieStore(cookieStore);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			json = sb.toString();
			log(json);
		} catch (Exception e) {
			log("Buffer Error converting result " + e.toString());
		}
		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			log("JSON Parser Error parsing data " + e.toString());
			try {
				jObj = new JSONObject(converStringToJsonObj(json));
			} catch (Exception e2) {
				// TODO: handle exception
				log("JSON String Error " + e2.toString());
			}

		}

		// return JSON String

		return jObj;

	}

	public String converStringToJsonObj(String str) {
		StringBuilder mstring = new StringBuilder();
		String[] mArr = str.split("&");

		mstring.append("{");
		for (int i = 0; i < mArr.length; i++) {
			String m = mArr[i];
			String[] sArr = m.split("=");
			m = "\"" + sArr[0] + "\"" + ":\"" + sArr[1] + "\"";
			if (i < mArr.length - 1) {
				mstring.append(m + ",");
			} else {
				mstring.append(m);
			}
		}
		mstring.append("}");
		return mstring.toString();
	}

	public String getStringFromUrl(String url, List<NameValuePair> params) {
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			json = sb.toString();
			log(json);
		} catch (Exception e) {
			log("Buffer Error converting result " + e.toString());
		}
		return json;
	}

	private void log(String msg) {
		if (DEBUG)
			LOGE("JSON PARSER", msg);
	}
}