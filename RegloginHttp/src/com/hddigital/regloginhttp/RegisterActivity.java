package com.hddigital.regloginhttp;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.hddigital.regloginhttp.data.Api;
import com.hddigital.regloginhttp.data.Api.RegisterInterface;
import com.hddigital.regloginhttp.data.StaticObject;
import com.hddigital.regloginhttp.data.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import static com.hddigital.regloginhttp.utils.LogUtils.*;

public class RegisterActivity extends Activity implements RegisterInterface {
	private static IOnRegistedListener mListener;
	private EditText emailTxt, passTxt, nameTxt, birthTxt, addressTxt, captchaTxt;
	private Button submitBtn;
	private ImageView imageCaptcha;
	private Builder alert;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		mContext = this;
		alert = new AlertDialog.Builder(mContext);
		
		Api.registerListener(this);
		initLayout();
		initCaptcha();
		initBirthday();
	}

	private void initLayout() {
		// TODO Auto-generated method stub
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		emailTxt = (EditText) findViewById(R.id.regEmail);
		passTxt = (EditText) findViewById(R.id.regPass);
		nameTxt = (EditText) findViewById(R.id.regName);
		birthTxt = (EditText) findViewById(R.id.regBirth);
		addressTxt = (EditText) findViewById(R.id.regAddress);
		captchaTxt = (EditText) findViewById(R.id.regCaptcha);
		submitBtn = (Button) findViewById(R.id.regSubmit);
		imageCaptcha = (ImageView) findViewById(R.id.regcaptchaImg);
		submitBtn.setOnClickListener(submitListener);

		imm.hideSoftInputFromWindow(emailTxt.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(passTxt.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(nameTxt.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(birthTxt.getWindowToken(), 3);
		imm.hideSoftInputFromWindow(addressTxt.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(captchaTxt.getWindowToken(), 0);

		emailTxt.setText("ai@bit.bit");
		passTxt.setText("123");
		nameTxt.setText("GOD OF BLESS YOU");
		addressTxt.setText("Address");
	}

	@SuppressLint("DefaultLocale")
	private void requestSignup() {

		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.add(new BasicNameValuePair("email", emailTxt.getText().toString()));
		mparams.add(new BasicNameValuePair("password", passTxt.getText().toString()));
		mparams.add(new BasicNameValuePair("name", nameTxt.getText().toString()));
		mparams.add(new BasicNameValuePair("age", birthTxt.getText().toString()));
		mparams.add(new BasicNameValuePair("address", addressTxt.getText().toString()));
		mparams.add(new BasicNameValuePair("scode", captchaTxt.getText().toString().toUpperCase()));
		log(captchaTxt.getText().toString());
		Api.registerUser(mparams);
	}

	@Override
	public void onError(String result) {

		if (result.equals("error_scode")) {
			showAlert("Sai mã an toàn vui lòng thử lại");
			initCaptcha();
		}
		if (result.equals("error_img")) {
			showAlert("Lỗi hình ảnh");
		}
		if (result.equals("error_email")) {
			showAlert("Email đã có người sử dụng");
			emailTxt.requestFocus();
		}
		if (result.equals("error_phone")) {
			showAlert("Số điện thoại đã dùng");
		}
		if (result.equals("done")) {
			if(mListener!=null)
				mListener.onRegisterDone();
			LoginActivity.mloginActivity.finish();
			this.finish();
		}

	}

	private View.OnClickListener submitListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (checkBlankField()) {
				if (isValidEmail(emailTxt.getText())) {
					requestSignup();
				} else {
					showAlert("Not valid email");
				}

			} else {
				showAlert("Please complete all fields");
			}
		}
	};

	private boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	private boolean checkBlankField() {
		// TODO Auto-generated method stub
		if (birthTxt.getText().toString().matches("") || nameTxt.getText().toString().matches("") || emailTxt.getText().toString().matches("")
				|| passTxt.getText().toString().matches("") || addressTxt.getText().toString().matches(""))
			return false;
		else
			return true;

	}

	private void showAlert(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	private void initCaptcha() {
		Bitmap captcha = null;
		try {
			captcha = loadCaptchaImg(StaticObject.captcha, 1);
		} catch (Exception e) {
			// TODO: handle exception
			log("ParseImageError" + e.toString());
			// Toast.makeText(getApplicationContext(), "Can't get captcha image", Toast.LENGTH_SHORT).show();
			alert.setMessage("Can't get captcha image");
			alert.show();
		}

		imageCaptcha.setImageBitmap(captcha);

	}

	private void initBirthday() {
		// TODO Auto-generated method stub
		birthTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					initDatepicker();
				}
			}
		});
	}

	private DatePickerDialog datePicker;
	private DatePickerDialog.OnDateSetListener datePickerListener;

	private String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	private void initDatepicker() {

		if (datePicker == null && datePickerListener == null) {
			datePickerListener = new DatePickerDialog.OnDateSetListener() {
				private Date userAge;

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					// updateBirthDay(year, monthOfYear,
					// dayOfMonth);
					String date = checkDigit(dayOfMonth) + "/" + checkDigit(monthOfYear + 1) + "/" + year;
					birthTxt.setText(date);

					addressTxt.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
					sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
					try {
						log(date);
						userAge = sdf.parse(date);
						log(userAge.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
			if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
				datePicker = new DatePickerDialog(mContext, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth, datePickerListener, 1983, 0, 1);
			else
				datePicker = new DatePickerDialog(mContext, datePickerListener, 1983, 0, 1);

		}
		datePicker.show();
	}

	private Bitmap loadCaptchaImg(String imgurl, int sampleSize) {

		DefaultHttpClient httpClient = new DefaultHttpClient();		
		CookieStore cookieStore = new BasicCookieStore();		
		Log.e("IMAGELOADER", "STATIC MCOOKIE: " + StaticObject.mCookie);
		if (StaticObject.mCookie != null)
			cookieStore.addCookie(StaticObject.mCookie);
		httpClient.setCookieStore(cookieStore);

		try {
			HttpGet httpRequest = new HttpGet(new URL(imgurl).toURI());
			HttpResponse response = (HttpResponse) httpClient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);

			final InputStream is = bufHttpEntity.getContent();
			try {
				final BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = sampleSize;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				return BitmapFactory.decodeStream(is, null, opts);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Problem reading image", e);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void log(String str) {
		LOGE("RegisterActivity", str);
	}
	public static void setRegListener(IOnRegistedListener listener){
		mListener = listener;
	}
	static interface IOnRegistedListener{
		public void onRegisterDone();
	}
}
