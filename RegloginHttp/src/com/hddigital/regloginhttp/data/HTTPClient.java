package com.hddigital.regloginhttp.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import static com.hddigital.regloginhttp.utils.LogUtils.*;

public class HTTPClient {
	public static void checkLogin() {
		log("checkLogin");
		try {
			new CheckUserLogin().execute(StaticObject.checklogin);
		} catch (Exception e) {
			// TODO: handle exception
			log(e.toString());
		}
	}

	public static void loginUser(String email, String password) {
		new LoginUser().execute(email, password);
	}

	public static void regUser(List<NameValuePair> regParams) {
		new RegUser().execute(regParams);
	}

	private static void log(String msg) {
		LOGE("HTTP Client", msg);
	}

	static class CheckUserLogin extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JSONObject islogined = new JSONParser().getJSONFromUrl(StaticObject.checklogin, null);
				return islogined;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String str = "";
			String name = "";
			String avatar = "";
			try {
				str = result.getString("message");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (str.equals("done")) {
				try {
					name = result.getString("name");
					avatar = result.getString("avatar");

				} catch (Exception e) {
					// TODO: handle exception
					log(e.toString());
				}
				User.setName(name);
				User.setAvatarLink(avatar);
			}
			Api.checkedLogin(str);
		}

	}

	static class LoginUser extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			// return null;
			JSONObject result = null;
			try {
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.add(new BasicNameValuePair("email", params[0]));
				mparams.add(new BasicNameValuePair("password", params[1]));

				result = new JSONParser().getJSONFromUrl(StaticObject.signin, mparams);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String status = "";
			try {
				status = result.getString("message");
				log(result.getString("message"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Api.onUserLoginHandler(status);
		}

	}

	static class RegUser extends AsyncTask<List<NameValuePair>, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			JSONObject result = null;
			try {
				result = new JSONParser().getJSONFromUrl(StaticObject.signup, params[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String message="";
			try{
				message=result.getString("message");				
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Api.onRegisterError(message);
		}

	}

}
