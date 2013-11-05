package com.hddigital.regloginhttp.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import static com.hddigital.regloginhttp.utils.LogUtils.*;

public class CookieParser {
	public CookieParser() {

	}

	public BasicClientCookie parser(String rawCookie) throws Exception {
		log(rawCookie);
		Pattern cookiePattern = Pattern.compile("([^=]+)=([^\\;]*);?\\s?");
		BasicClientCookie cookie = null;
		Matcher matcher = cookiePattern.matcher(rawCookie);
		log("totalMatcher: "+matcher.toString());
		while (matcher.find()) {
			int groupCount = matcher.groupCount();
			System.out.println("matched: " + matcher.group(0));
			for (int groupIndex = 0; groupIndex <= groupCount; ++groupIndex) {
				System.out.println("group[" + groupIndex + "]=" + matcher.group(groupIndex));
			}
			String cookieKey = matcher.group(1);
			String cookieValue = matcher.group(2);
			cookie = new BasicClientCookie(cookieKey, cookieValue);

			// modify other cookie properties based on remaing match groups using
			// using Doug's code encapsulated into a separate method
		}
		log("get: " + cookie);
		/*String pattern = "([][])\\s";
		String mrawCookie = rawCookie.replaceAll(pattern, "");
		log("replacement: " + mrawCookie);
		String[] rawCookieParams = rawCookie.split("\\][\\)");

		rawCookieParams[0].substring(0, 1);
		rawCookieParams[rawCookieParams.length - 1].substring(rawCookieParams[rawCookieParams.length - 1].length() - 1);

		log(rawCookieParams.toString());

		String[] rawCookieName = rawCookieParams[0].split(":");
		if (rawCookieName.length != 2) {
			throw new Exception("Invalid cookie: missing name and value.");
		}

		String cookieName = rawCookieName[1].trim();
		String[] rawCookieValue = rawCookieParams[1].split(":");
		String cookieValue = rawCookieValue[1];

		BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);

		for (int i = 2; i < rawCookieParams.length - 1; i++) {
			String rawCookieParamNameAndValue[] = rawCookieParams[i].trim().split(":");

			String paramName = rawCookieParamNameAndValue[0].trim();

			if (paramName.equalsIgnoreCase("secure")) {
				cookie.setSecure(true);
			} else {
				if (rawCookieParamNameAndValue.length != 2) {
					throw new Exception("Invalid cookie: attribute not a flag or missing value.");
				}

				String paramValue = rawCookieParamNameAndValue[1].trim();

				if (paramName.equalsIgnoreCase("domain")) {
					cookie.setDomain(paramValue);
				} else if (paramName.equalsIgnoreCase("path")) {
					cookie.setPath(paramValue);
				}

				if (paramName.equalsIgnoreCase("expires")) {
					Date expiryDate = (Date) DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).parse(paramValue);
					cookie.setExpiryDate(expiryDate);
				} else if (paramName.equalsIgnoreCase("max-age")) {
					long maxAge = Long.parseLong(paramValue);
					Date expiryDate = new Date(System.currentTimeMillis() + maxAge);
					cookie.setExpiryDate(expiryDate);
				} else if (paramName.equalsIgnoreCase("comment")) {
					cookie.setPath(paramValue);
				} else {
					throw new Exception("Invalid cookie: invalid attribute name.");
				}
			}
		}
		String rawPramAndValue[] = rawCookieParams[rawCookieParams.length - 1].trim().split(":");
		String paramName = rawPramAndValue[0];
		String paramValue = "";
		for (int n = 1; n < rawPramAndValue.length; n++) {
			if (n < rawPramAndValue.length - 1)
				paramValue += rawPramAndValue[n] + ":";
			else
				paramValue += rawPramAndValue[n];
		}
		if (paramName.equalsIgnoreCase("expires")) {
			Date expiryDate = (Date) DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).parse(paramValue);
			cookie.setExpiryDate(expiryDate);
		}*/

		return cookie;
	}

	private void log(String msg) {
		LOGE("CookieParser", msg);
	}
}
