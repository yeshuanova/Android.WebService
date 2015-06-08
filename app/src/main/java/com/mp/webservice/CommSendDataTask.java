package com.mp.webservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class CommSendDataTask extends AsyncTask<CommStatusBase, Void, String> {

	private boolean _comm_success = false;
	private SendHttpCompleteCallBack _send_complete = null;
	private static final int TIME_OUT_CONN = 5000;
	private static final int TIME_OUT_READ = 10000;
	
	public interface SendHttpCompleteCallBack {
		public void onSendHtttpComplete(boolean isSuccess, String result);
	}

	public CommSendDataTask(SendHttpCompleteCallBack complete_method) {
		super();
		this._send_complete = complete_method;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(CommStatusBase... data) {
		String res_str = "";
		_comm_success = false;
		try {
			
			CommStatusBase comm_data = data[0];
			
			Log.i(this.getClass().getName(), "URL : " + comm_data.getSendURL());
			URL url = new URL(comm_data.getSendURL());
			HttpURLConnection conn_url = (HttpURLConnection)url.openConnection();
			conn_url.setConnectTimeout(TIME_OUT_CONN);
			conn_url.setReadTimeout(TIME_OUT_READ);
			
			if (comm_data.isHttpPost()) {
				conn_url.setDoInput(true);
				conn_url.setDoOutput(true);
				OutputStream os = conn_url.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				Log.i(this.getClass().getName(), "POST DATA : " + comm_data.getPostData());
				writer.write(comm_data.getPostData());
				writer.flush();
				writer.close();
				os.close();
			} else {
				conn_url.setDoOutput(false);
			}
			
			InputStream input_stream = conn_url.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input_stream, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			res_str = builder.toString();
			reader.close();
			
			conn_url.disconnect();
			_comm_success = true;
		} catch (Exception e) {
			e.printStackTrace();
			res_str = e.toString();
			Log.w(this.getClass().getName(), "Exception: Exception:\n" + e.toString());
		}
		
		return res_str;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (null != this._send_complete) {
			this._send_complete.onSendHtttpComplete(this.isSuccess(), result);
		}
	}

	public boolean isSuccess() {
		return this._comm_success;
	}

}
