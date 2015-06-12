package com.mp.webservice.comm;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mp.webservice.StatusHttpBin;
import com.mp.webservice.comm.task.CommRequestMessageTask;

public class CommRequestJsonMsg<SendType, ReturnType> implements CommRequestMessageTask.SendHttpCompleteCallBack {
	private RequestDataCallback<ReturnType> _callback;
	private TypeToken<SendType> _send_type_token;
	private TypeToken<ReturnType> _return_type_token;
	private SendType _send_data;
	private CommStatusBase _comm_obj = new StatusHttpBin();
	private RequestDataFinalAction _final_action;
	
	public interface RequestDataCallback<ReturnType> {
		void onRequestDataSuccess(ReturnType return_data);
		void onRequestDataFailed(String fail_msg);
	}
	
	public interface RequestDataFinalAction {
		void onRequestFinally(boolean is_success);
	}
	
	public CommRequestJsonMsg(
			SendType send_data,
			RequestDataCallback<ReturnType> callback,
			TypeToken<SendType> send_type_token,
			TypeToken<ReturnType> return_type_token) {
		
		setSendData(send_data);
		setRequestCallback(callback);
		setSendDataTypeToken(send_type_token);
		setReturnDataTypeToken(return_type_token);
	}
	
	public void setSendData(SendType send_data) {
		_send_data = send_data;
	}
	
	public void setRequestCallback(RequestDataCallback<ReturnType> call_back) {
		_callback = call_back;
	}
	
	public void setSendDataTypeToken(TypeToken<SendType> type_token) {
		_send_type_token = type_token;
	}
	
	public void setReturnDataTypeToken(TypeToken<ReturnType> type_token) {
		_return_type_token = type_token;
	}
	
	public void setFinalAction(RequestDataFinalAction final_action) {
		_final_action = final_action;
	}
	
	public void setCommObj(CommStatusBase obj) {
		_comm_obj = obj;
	}
	
	public CommStatusBase getCommObj() {
		return _comm_obj;
	}
	
	public void runSendData() {
		_comm_obj.setDataString(new Gson().toJson(_send_data, _send_type_token.getType()));
		
		CommRequestMessageTask send_data_http = new CommRequestMessageTask(this);
		send_data_http.execute(_comm_obj);
	}

	@Override
	public void onSendHtttpComplete(boolean isSuccess, String result) {

		boolean is_convert_success = false;
		if (isSuccess) {
			try {
				Log.i(this.getClass().getName(), "Return Str : \n" + result);
				ReturnType return_data = new Gson().getAdapter(_return_type_token).fromJson(result);
				_callback.onRequestDataSuccess(return_data);
				is_convert_success = true;
			} catch (JsonSyntaxException e) {
				Log.w(this.getClass().getName(), "JsonSyntaxException: " + e.toString());
				e.printStackTrace();
				_callback.onRequestDataFailed(e.toString());
			} catch (Exception e) {
				Log.w(this.getClass().getName(), "Exception: " + e.toString());
				e.printStackTrace();
				_callback.onRequestDataFailed(e.toString());
			}
		} else {
			try {
				this._callback.onRequestDataFailed(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (null != this._final_action) {
			this._final_action.onRequestFinally(is_convert_success);
		}

	}

}
