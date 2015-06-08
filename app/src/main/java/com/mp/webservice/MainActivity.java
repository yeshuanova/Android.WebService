package com.mp.webservice;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mp.webservice.CommChainManager.MODE;
import com.mp.webservice.CommStatusBase.CommType;
import com.mp.webservice.HttpBinData.HttpBinRequest;
import com.mp.webservice.HttpBinData.HttpBinResponse;

public class MainActivity extends FragmentActivity {
	
	TextView _msg_view;
	Button _send_get;
	Button _send_post;
	
	class SendHttpGetListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			
			HttpBinRequest request_data = new HttpBinRequest();
			CommRequest< ?, ?> request = new CommRequest<HttpBinRequest, HttpBinResponse>(
					request_data,
					new SendHttpGetCallback(), 
					new TypeToken<HttpBinRequest>() {},
					new TypeToken<HttpBinResponse>() {});
			
			CommStatusBase http_bin = new CommStatusHttpBin();
			http_bin.setHttpType(CommType.HttpGet);
			http_bin.setURL(getString(R.string.bin_http_get_url));
			request.setCommObj(http_bin);
			
			
			CommChainManager req_mgr = new CommChainManager();
			req_mgr.setMode(MODE.OVERALL);
			req_mgr.addRequest(request);
			
			CommProgressDialog dlg = new CommProgressDialog(MainActivity.this, "Http/Get Connect ...", req_mgr);
			dlg.runProgressTask();
		}
	}
	
	private Gson getFormatGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().create();
	}
	
	class SendHttpGetCallback implements CommRequest.RequestDataCallback<HttpBinResponse> {

		@Override
		public void onRequestDataSuccess(HttpBinResponse return_data) {
			_msg_view.setText(getFormatGsonBuilder().toJson(return_data));
		}

		@Override
		public void onRequestDataFailed(String fail_msg) {
			_msg_view.setText(fail_msg);
		}
		
	}
	
	class SendHttpPostListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			HttpBinRequest request_data = new HttpBinRequest();
			CommRequest< ?, ?> request = new CommRequest<HttpBinRequest, HttpBinResponse>(
					request_data,
					new SendHttpGetCallback(), 
					new TypeToken<HttpBinRequest>() {},
					new TypeToken<HttpBinResponse>() {});
			
			CommStatusHttpBin http_bin = new CommStatusHttpBin();
			http_bin.setHttpType(CommType.HttpPost);
			http_bin.setURL(getString(R.string.bin_http_post_url));
			request.setCommObj(http_bin);
			
			CommChainManager req_mgr = new CommChainManager();
			req_mgr.setMode(MODE.OVERALL);
			req_mgr.addRequest(request);
			
			CommProgressDialog dlg = new CommProgressDialog(MainActivity.this, "Http/Post Connect ...", req_mgr);
			dlg.runProgressTask();
		}
	}
	
	class SendHttpPostCallback implements CommRequest.RequestDataCallback<HttpBinResponse> {

		@Override
		public void onRequestDataSuccess(HttpBinResponse return_data) {
			_msg_view.setText(getFormatGsonBuilder().toJson(return_data));
		}

		@Override
		public void onRequestDataFailed(String fail_msg) {
			_msg_view.setText(fail_msg);
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_msg_view = (TextView) findViewById(R.id.msg_text);
		_send_get = (Button) findViewById(R.id.send_get);
		_send_post = (Button) findViewById(R.id.send_post);
		
		_send_get.setOnClickListener(new SendHttpGetListener());
		_send_post.setOnClickListener(new SendHttpPostListener());
		
		_msg_view.setText(getFormatGsonBuilder().toJson(new HttpBinRequest()));
	}
}
