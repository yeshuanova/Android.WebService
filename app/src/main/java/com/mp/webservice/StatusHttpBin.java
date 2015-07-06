package com.mp.webservice;

import android.util.Log;

import webservice.CommStatusBase;

import java.net.URLEncoder;

public class StatusHttpBin extends CommStatusBase {

	public static final String DATA_KEY = "json_str";
	
	public String getRequestURL() {
		String send_str = getOriginalURL();
		try {
			if (isHttpGet()) {
				return getOriginalURL() + "?" + DATA_KEY + "=" + URLEncoder.encode(getDataString(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w(this.getClass().getName(), e.toString());
		}
		return send_str;
	}
	
	public String getPostString() {
		if (isHttpPost()) {
			return DATA_KEY + "=" + getDataString();
		}
		return "";
	}

}
