package com.misa.test.app;

import java.util.List;

import com.misa.droid.framework.app.MisaApplication;
import com.misa.droid.framework.helper.DbHelper;

import java.security.interfaces.RSAPublicKey;

public class PhotoDemoApplication extends MisaApplication
{
	private static DbHelper helper;
	private static String uid;
	//public static final String address = "192.168.0.23";
	//public static final String address = "192.168.0.139";
	public static final String address = "182.254.244.222";
	public static final String port = "9710";
	public static List<RSAPublicKey> publicKeyList;		//¹«Ô¿ÁÐ±í
	
	public static String phoneId;

	public static void setDbHelper(DbHelper h)
	{
		helper = h;
	}
	
	public static DbHelper getDbHelper()
	{
		return helper;
	}

	public static String getUid() {
		return uid;
	}

	public static void setUid(String uid) {
		PhotoDemoApplication.uid = uid;
	}
}
