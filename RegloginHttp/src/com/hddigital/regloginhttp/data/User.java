package com.hddigital.regloginhttp.data;

public class User {
	private static String Name;
	private static String avatarLink;
	private static String session;
	

	public static void setName(String str) {
		Name = str;
	}

	public static String getName() {
		return Name;
	}

	public static void setAvatarLink(String link) {
		avatarLink = link;
	}

	public static String getAvatar() {
		return avatarLink;
	}

	public static void setSession(String str) {
		session = str;
	}

	public static String getSession() {
		return session;
	}

}
