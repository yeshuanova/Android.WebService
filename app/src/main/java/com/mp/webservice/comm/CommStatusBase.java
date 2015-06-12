package com.mp.webservice.comm;

import java.io.UnsupportedEncodingException;

public abstract class CommStatusBase {

	protected String _url = "";
	protected String _data_str = "";
	protected CommType _comm_type = CommType.HttpPost;

	public enum CommType {
		HttpPost, HttpGet
	}

	public String getSendURL() throws UnsupportedEncodingException {
		return "";
	}

	public String getPostData() {
		return "";
	}

	public void setDataString(String str) {
		this._data_str = str;
	}

	public String getDataString() {
		return _data_str;
	}

	public void setHttpType(CommType type) {
		_comm_type = type;
	}

	public boolean isHttpPost() {
		return CommType.HttpPost == this._comm_type;
	}

	public boolean isHttpGet() {
		return CommType.HttpGet == this._comm_type;
	}

	public void setURL(String url) {
		_url = url;
	}

	public String getURL() {
		return _url;
	}

}
