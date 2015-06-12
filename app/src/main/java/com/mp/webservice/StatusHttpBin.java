package com.mp.webservice;

import com.mp.webservice.comm.CommStatusBase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StatusHttpBin extends CommStatusBase {

	public static final String DATA_KEY = "json_str";
	
	public String getSendURL() throws UnsupportedEncodingException {
		if (isHttpGet()) {
			return getURL() + "?" + DATA_KEY + "=" + URLEncoder.encode(getDataString(), "UTF-8");
		}
		return _url;
	}
	
	public String getPostData() {
		if (isHttpPost()) {
			return DATA_KEY + "=" + _data_str;
		}
		return "";
	}

}