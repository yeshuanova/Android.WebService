package csl.ws.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mp.webservice.R;

import csl.ws.example.HttpBinData.HttpBinRequest;
import csl.ws.example.HttpBinData.HttpBinResponse;
import webservice.CommBaseStatus.CommType;
import webservice.CommChainManager;
import webservice.CommChainManager.MODE;
import webservice.CommProgressDialog;
import webservice.CommRequestGetUrlBitmap;
import webservice.CommRequestJsonMsg;

public class MainActivity extends Activity {

	private TextView _msg_view;
	private ImageView _image_view;

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

	class ClickGetImageBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			CommRequestGetUrlBitmap request = new CommRequestGetUrlBitmap(img_url, new GetImageAction());
			CommChainManager req_mgr = new CommChainManager();
			req_mgr.setMode(MODE.OVERALL);
			req_mgr.addRequest(request);

			CommProgressDialog dlg = new CommProgressDialog(MainActivity.this, "Loading image ...", req_mgr);
			dlg.runProgressTask();
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

	class ResetDataClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			_image_view.setImageBitmap(null);
			_msg_view.setText("");
		}
	}

	private CommRequestJsonMsg getHttpGetRequest() {

		HttpBinStatus http_bin = new HttpBinStatus();
		http_bin.setHttpType(CommType.HttpGet);
		http_bin.setOriginalURL(getString(R.string.bin_http_get_url));
		http_bin.setDataString(new Gson().toJson(new HttpBinRequest()));

		CommRequestJsonMsg request = new CommRequestJsonMsg<>(
				new SendHttpGetCallback(),
				new TypeToken<HttpBinResponse>(){},
				http_bin
		);

		return request;
	}

	private CommRequestJsonMsg getHttpPostRequest() {

		HttpBinStatus http_bin = new HttpBinStatus();
		http_bin.setHttpType(CommType.HttpPost);
		http_bin.setOriginalURL(getString(R.string.bin_http_post_url));
		http_bin.setDataString(new Gson().toJson(new HttpBinRequest()));

		CommRequestJsonMsg request = new CommRequestJsonMsg<>(
				new SendHttpGetCallback(),
				new TypeToken<HttpBinResponse>(){},
				http_bin
		);

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
		Button _send_get = (Button) findViewById(R.id.send_get);
		Button _send_post = (Button) findViewById(R.id.send_post);

		_image_view = (ImageView) findViewById(R.id.image_view);

		Button _get_image = (Button) findViewById(R.id.get_image);
		Button _request_chain = (Button) findViewById(R.id.request_chain);

		_send_get.setOnClickListener(new SendHttpGetListener());
		_send_post.setOnClickListener(new SendHttpPostListener());
		
		_msg_view.setText(getFormatGsonBuilder().toJson(new HttpBinRequest()));

		_get_image.setOnClickListener(new ClickGetImageBtnListener());
		_request_chain.setOnClickListener(new CallRequestChainListener());

		Button _reset_btn = (Button)findViewById(R.id.reset_data);
		_reset_btn.setOnClickListener(new ResetDataClickListener());

	}
}
