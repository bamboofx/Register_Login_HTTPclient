package com.hddigital.regloginhttp.data;

import static com.hddigital.regloginhttp.utils.LogUtils.*;

import java.util.List;

import org.apache.http.NameValuePair;

public class Api {

	private static ApiUpdate apiListener;
	private static RegisterInterface registerListener;

	public static void checkLogin() {
		log("checkLogin");
		HTTPClient.checkLogin();
	}

	public static void checkedLogin(String result) {
		apiListener.onLoginChecked(result);
	}

	public static void onUserLoginHandler(String result) {
		apiListener.onLoginHandler(result);

	}

	public static void loginUser(String email, String password) {
		HTTPClient.loginUser(email, password);
		apiListener.onRequestHttp();
	}

	public static void registerUser(List<NameValuePair> regParams) {
		HTTPClient.regUser(regParams);
		apiListener.onRequestHttp();
	}

	public static void setListener(ApiUpdate listener) {
		apiListener = listener;
	}

	public static void registerListener(RegisterInterface listener) {
		registerListener = listener;
	}

	public static void onRegisterError(String result) {
		log("RegisterResult: " + result);
		registerListener.onError(result);
	}

	private static void log(String msg) {
		LOGE("API", msg);
	}

	public static interface ApiUpdate {
		public void onLoginChecked(String result);

		public void onLoginHandler(String result);

		public void onRequestHttp();

		public void onRegistedHandler();
	}

	public static interface RegisterInterface {
		public void onError(String result);
	}
}
