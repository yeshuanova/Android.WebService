package com.mp.webservice;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mp.webservice.HttpBinData.HttpBinRequest;
import com.mp.webservice.HttpBinData.HttpBinResponse;
import com.mp.webservice.comm.CommChainManager;
import com.mp.webservice.comm.CommChainManager.MODE;
import com.mp.webservice.comm.CommProgressDialog;
import com.mp.webservice.comm.CommRequestGetUrlBitmap;
import com.mp.webservice.comm.CommRequestJsonMsg;
import com.mp.webservice.comm.CommStatusBase;
import com.mp.webservice.comm.CommStatusBase.CommType;
import com.mp.webservice.comm.ServerResponse;

import java.io.UnsupportedEncodingException;

import static com.mp.webservice.comm.MessageContainer.ApiLoginRequest;
import static com.mp.webservice.comm.MessageContainer.ApiLoginResponse;
//import static com.mp.webservice.comm.MessageContainer.ApiLoginRequest;

public class MainActivity extends FragmentActivity {

	private TextView _msg_view;
	private Button _send_get;
	private Button _send_post;
	private ImageView _image_view;
	private Button _get_image;
	private Button _request_chain;

	private static String img_url = "http://i.imgur.com/BN8JhJc.jpg";
	
	class SendHttpGetListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			
			CommRequestJsonMsg request = getHttpGetRequest();
			
			CommChainManager req_mgr = new CommChainManager();
			req_mgr.setMode(MODE.OVERALL);
			req_mgr.addRequest(request);
			
			CommProgressDialog dlg = new CommProgressDialog(MainActivity.this, "Http/Get Connect ...", req_mgr);
			dlg.runProgressTask();
		}
	}

	class SendHttpGetCallback implements CommRequestJsonMsg.RequestJsonMsgCallback<HttpBinResponse> {

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

			CommRequestJsonMsg request = getHttpPostRequest();

			CommChainManager req_mgr = new CommChainManager();
			req_mgr.setMode(MODE.OVERALL);
			req_mgr.addRequest(request);
			
			CommProgressDialog dlg = new CommProgressDialog(MainActivity.this, "Http/Post Connect ...", req_mgr);
			dlg.runProgressTask();
		}
	}

	class ClickCommTestListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			ApiLoginRequest req_data = new ApiLoginRequest();
			req_data.set_api_uid(getString(R.string.ublms_api_uid));
			req_data.set_api_pwd(getString(R.string.ublms_api_pwd));

			CommRequestJsonMsg<? , ?> request;
			request = new CommRequestJsonMsg<>(
                    req_data,
                    new CompleteCallback(),
                    new TypeToken<ApiLoginRequest>() {},
                    new TypeToken<ServerResponse<ApiLoginResponse>>() {}
			);
			request.setCommObj(new UblmsCommStatus());
			request.runRequest();
		}

		class CompleteCallback implements CommRequestJsonMsg.RequestJsonMsgCallback<ServerResponse<ApiLoginResponse>> {

			@Override
			public void onRequestDataSuccess(ServerResponse<ApiLoginResponse> return_data) {
				String str = new Gson().toJson(return_data);
				Log.i(getClass().getName(), "Login Response String : \n" + str);
				_msg_view.setText(str);
			}

			@Override
			public void onRequestDataFailed(String fail_msg) {
				Log.i(getClass().getName(), "Login Response test error :\n" + fail_msg);
				_msg_view.setText(fail_msg);
			}
		}

		class UblmsCommStatus extends CommStatusBase {

			private String _sid = "";
			private String _api_name = "login";
			private String _dest_url = "http://59.120.234.78/api/";

			private static final String POST_SID = "sid";
			private static final String POST_NAME_API = "api";
			private static final String POST_DATA = "datas";
			private static final String POST_RESTYPE = "restype";
			private static final String RESTYPE_JSON = "json";

			@Override
			public String getSendURL() throws UnsupportedEncodingException {
				return _dest_url;
			}

			@Override
			public String getPostData() {
				StringBuilder str_builder = new StringBuilder();
				str_builder.append(POST_SID + "=" + this._sid + "&");
				str_builder.append(POST_NAME_API + "=" + this._api_name + "&");
				str_builder.append(POST_DATA + "=" + getDataString() + "&");
				str_builder.append(POST_RESTYPE + "=" + RESTYPE_JSON);
				return str_builder.toString();
			}
		}
	}

	class ClickGetImageBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			CommRequestGetUrlBitmap request = new CommRequestGetUrlBitmap(img_url, new GetImageAction());
			request.runRequest();
		}
	}

	class GetImageAction implements CommRequestGetUrlBitmap.IRequestGetUrlBitmapAction {

		@Override
		public void onSuccess(Bitmap bmp) {
			_image_view.setImageBitmap(bmp);
		}

		@Override
		public void onFailure(String msg) {
			Log.w(this.getClass().getName(), msg);
		}
	}

	class CallRequestChainListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {

			CommRequestGetUrlBitmap req_bmp = new CommRequestGetUrlBitmap(img_url, new GetImageAction());
			CommRequestJsonMsg req_get = getHttpGetRequest();
			CommRequestJsonMsg req_post = getHttpPostRequest();

			CommChainManager chain_mgr = new CommChainManager();
			chain_mgr.setMode(MODE.SEQUENCE);
			chain_mgr.addRequest(req_get);
			chain_mgr.addRequest(req_bmp);
			chain_mgr.addRequest(req_post);

			CommProgressDialog prog_dlg = new CommProgressDialog(MainActivity.this, "Run Request Chain", chain_mgr);
			prog_dlg.runProgressTask();
		}
	}

	private CommRequestJsonMsg getHttpRequest() {

		HttpBinRequest request_data = new HttpBinRequest();
		CommRequestJsonMsg request = new CommRequestJsonMsg<>(
				request_data,
				new SendHttpGetCallback(),
				new TypeToken<HttpBinRequest>() {},
				new TypeToken<HttpBinResponse>() {}
		);
		return request;
	}

	private CommRequestJsonMsg getHttpGetRequest() {

		CommRequestJsonMsg request = getHttpRequest();
		CommStatusBase http_bin = new StatusHttpBin();
		http_bin.setHttpType(CommType.HttpGet);
		http_bin.setURL(getString(R.string.bin_http_get_url));
		request.setCommObj(http_bin);

		return request;
	}

	private CommRequestJsonMsg getHttpPostRequest() {

		CommRequestJsonMsg request = getHttpRequest();

		StatusHttpBin http_bin = new StatusHttpBin();
		http_bin.setHttpType(CommType.HttpPost);
		http_bin.setURL(getString(R.string.bin_http_post_url));
		request.setCommObj(http_bin);

		return request;
	}

	private Gson getFormatGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_msg_view = (TextView) findViewById(R.id.msg_text);
		_send_get = (Button) findViewById(R.id.send_get);
		_send_post = (Button) findViewById(R.id.send_post);
		_image_view = (ImageView) findViewById(R.id.image_view);
		_get_image = (Button) findViewById(R.id.get_image);
		_request_chain = (Button) findViewById(R.id.request_chain);

		_send_get.setOnClickListener(new SendHttpGetListener());
		_send_post.setOnClickListener(new SendHttpPostListener());
		
		_msg_view.setText(getFormatGsonBuilder().toJson(new HttpBinRequest()));

		_get_image.setOnClickListener(new ClickGetImageBtnListener());
//		_request_chain.setOnClickListener(new CallRequestChainListener());

		_request_chain.setOnClickListener(new ClickCommTestListener());

	}
}
