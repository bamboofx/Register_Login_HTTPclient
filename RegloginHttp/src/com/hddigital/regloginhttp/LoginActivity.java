package com.hddigital.regloginhttp;

import static com.hddigital.regloginhttp.utils.LogUtils.LOGE;

import com.hddigital.regloginhttp.data.Api;
import com.hddigital.regloginhttp.data.StaticObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	public static LoginActivity mloginActivity;
	private EditText email, password;
	private CheckBox checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mloginActivity = this;
		setContentView(R.layout.login_layout);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		email = (EditText) findViewById(R.id.loginEmail);
		password = (EditText) findViewById(R.id.loginPass);
		checkBox = (CheckBox) findViewById(R.id.loginCheckBox);
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
		log("onCreate");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void savingPreferences() {

		SharedPreferences pre = getSharedPreferences(StaticObject.prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		String user = email.getText().toString();
		String pwd = password.getText().toString();
		boolean bchk = checkBox.isChecked();
		if (!bchk) {
			editor.clear();
		} else {
			editor.putString("user", user);
			editor.putString("pwd", pwd);
			editor.putBoolean("checked", bchk);
		}
		editor.commit();
	}

	public void restoringPreferences() {
		SharedPreferences pre = getSharedPreferences(StaticObject.prefName, MODE_PRIVATE);

		boolean bchk = pre.getBoolean("checked", false);
		if (bchk) {
			String user = pre.getString("user", "");
			String pwd = pre.getString("pwd", "");
			email.setText(user);
			password.setText(pwd);
			Api.loginUser(user, pwd);
			this.finish();
		}
		checkBox.setChecked(bchk);
	}

	public void onSubmit(View v) {
		this.finish();
		if (email.getText().length() == 0) {
			Api.loginUser("tao@tao.com", "123");
		} else {
			Api.loginUser(email.getText().toString(), password.getText().toString());
		}
	}

	public void onRegister(View v) {
		Intent regIntent = new Intent(this, RegisterActivity.class);
		startActivity(regIntent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		log("onResume");
		restoringPreferences();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("onStop");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		savingPreferences();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mloginActivity = null;
		log("onDestroy");
	}

	private static void log(String msg) {
		LOGE("Login Activity", msg);
	}
}
