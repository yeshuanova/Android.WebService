package com.mp.webservice;

import com.google.gson.annotations.SerializedName;

public class HttpBinData {
	public static class HttpBinRequest {
		@SerializedName("string_data")
		public String str = "To be, or not to be.";
		@SerializedName("double_num")
		public double _d = 3.1415;
		@SerializedName("float_num")
		public float _f = 2.71828f;
		@SerializedName("integer_num")
		public int _i = 42;
	}

	public static class HttpBinHeaderData {
		public String Accept = "";

		@SerializedName("Accept-Encoding")
		public String Accept_Encoding = "";

		@SerializedName("Accept-Language")
		public String Accept_Language = "";
		public String Host = "";
		@SerializedName("User-Agent")
		public String User_Agent = "";

	}

	public static class HttpBinResponse {
		public RequestDataStr args;
		public RequestDataStr form;
		public HttpBinHeaderData headers = new HttpBinHeaderData();
		public String origin = "";
		public String url = "";
		
		public static class RequestDataStr {
			public String json_str = "Default";
		}
	}
}
