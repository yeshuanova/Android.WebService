package csl.ws.example;

import android.util.Log;

import java.net.URLEncoder;

import webservice.CommBaseStatus;

public class HttpBinStatus extends CommBaseStatus {

	public static final String DATA_KEY = "json_str";
	private String _data_str = "";

	public HttpBinStatus() {

	}

	public void setDataString(String str) {
		_data_str = str;
	}
	
	public String getRequestURL() {
		String send_str = getOriginalURL();
		try {
			if (isHttpGet()) {
				return getOriginalURL() + "?" + DATA_KEY + "=" + URLEncoder.encode(_data_str, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w(this.getClass().getName(), e.toString());
		}
		return send_str;
	}
	
	public String getPostString() {
		if (isHttpPost()) {
			return DATA_KEY + "=" + _data_str;
		}
		return "";
	}

}
