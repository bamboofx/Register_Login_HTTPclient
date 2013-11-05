package com.hddigital.regloginhttp;

import org.apache.http.cookie.Cookie;

import com.hddigital.regloginhttp.RegisterActivity.IOnRegistedListener;
import com.hddigital.regloginhttp.data.Api;
import com.hddigital.regloginhttp.data.StaticObject;
import com.hddigital.regloginhttp.data.Api.ApiUpdate;
import com.hddigital.regloginhttp.data.User;
import com.hddigital.regloginhttp.utils.CookieParser;
import com.hddigital.regloginhttp.utils.CookieUtils;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.TextView;
import static com.hddigital.regloginhttp.utils.LogUtils.*;

public class MainActivity extends Activity implements ApiUpdate, IOnRegistedListener {

	private Context mContext;
	private ProgressDialog progressDl;
	private boolean loginCheked = false;
	private Builder alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Api.setListener(this);
		RegisterActivity.setRegListener(this);
		mContext = this;

		progressDl = new ProgressDialog(mContext);
		progressDl.setMessage("Loading...");
		progressDl.setCanceledOnTouchOutside(false);
		progressDl.setCancelable(false);

		alert = new AlertDialog.Builder(mContext);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		log("onCreate");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		log("onResume");
		if (!loginCheked) {
			initialize();
		}
		restoreCookie();
	}

	private void restoreCookie() {
		/*if (StaticObject.mCookie == null)
			StaticObject.mCookie = CookieUtils.getCookie(mContext);*/

		/*

		if (!sCookie.matches("")) {
			Cookie mCookie = null;
			try {
				mCookie = new CookieParser().parser(sCookie);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mCookie != null) {
				StaticObject.mCookie = mCookie;
				log("StoredCookie: " + mCookie.toString());
			}
		}*/
	}

	private void initialize() {
		// TODO Auto-generated method stub
		progressDl.show();
		log("initialize");
		Api.checkLogin();
	}

	@Override
	public void onLoginChecked(String result) {
		// TODO Auto-generated method stub
		progressDl.hide();
		// log("is equals?:" + result + "||" + (result == "error_notlogin"));
		loginCheked = true;
		if (result.equals("error_notlogin")) {
			Intent loginIntent = new Intent(mContext, LoginActivity.class);
			startActivity(loginIntent);
			if (StaticObject.mCookie != null) {

				/*SharedPreferences pre = getSharedPreferences(StaticObject.cookieData, MODE_PRIVATE);
				SharedPreferences.Editor editor = pre.edit();
				editor.putString("cookie", StaticObject.mCookie.toString());
				editor.commit();*/
				//CookieUtils.setCookie(mContext, StaticObject.mCookie);

			}
		}
		if (result.equals("done")) {
			TextView txt = (TextView) findViewById(R.id.mainText);
			txt.setText("Login Successful:\n hello " + User.getName());
		}
	}

	@Override
	public void onRegisterDone() {
		// TODO Auto-generated method stub
		Api.checkLogin();
	}

	@Override
	public void onLoginHandler(String result) {
		// TODO Auto-generated method stub
		progressDl.hide();
		if (result.equals("done")) {
			Api.checkLogin();
		}
		if (result.equals("error_email")) {
			alert.setMessage("error_email");
			alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					Intent loginIntent = new Intent(mContext, LoginActivity.class);
					startActivity(loginIntent);
				}
			});
			alert.show();
		}
		if (result.equals("error_password")) {
			alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					Intent loginIntent = new Intent(mContext, LoginActivity.class);
					startActivity(loginIntent);
				}
			});
			alert.setMessage("error_password");
			alert.show();
		}
		if (result.equals("error_data")) {
			alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					Intent loginIntent = new Intent(mContext, LoginActivity.class);
					startActivity(loginIntent);
				}
			});
			alert.setMessage("error_data");
			alert.show();
			SharedPreferences pre = getSharedPreferences(StaticObject.prefName, MODE_PRIVATE);
			SharedPreferences.Editor editor = pre.edit();
			editor.clear();
			editor.commit();
		}
	}

	@Override
	public void onRequestHttp() {
		// TODO Auto-generated method stub
		progressDl.show();
	}

	@Override
	public void onRegistedHandler() {
		// TODO Auto-generated method stub
		progressDl.hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (progressDl != null)
			progressDl.dismiss();
		log("onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (progressDl != null)
			progressDl.dismiss();
		log("onDestroy");
	}

	private void log(String msg) {
		LOGE("MainActivity", msg);
	}
}
