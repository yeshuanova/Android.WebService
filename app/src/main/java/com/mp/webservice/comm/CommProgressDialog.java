package com.mp.webservice.comm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

public class CommProgressDialog {
	
	private ProgressDialog _prog_dlg = null;
	private CommChainManager _request_manager;
	private int _delay_time = 2000;
	private boolean _is_send_finish = false;
	private boolean _is_delay_finish = false;
	
	class RequestChainCompleteAction implements CommChainManager.OnRequestChainComplete {

		@Override
		public void onRequestChainComplete(boolean is_success) {
			Log.d(this.getClass().getName(), "Run ProgressDialog request completion");
			_is_send_finish = true;
			checkFinish();
		}
	}

	class TimeDelayAsyncTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				Thread.sleep(params[0]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			_is_delay_finish = true;
			checkFinish();
		}
	}
	
	public CommProgressDialog(Context context, String msg, CommChainManager manager) {
		_prog_dlg = new ProgressDialog(context);
		_prog_dlg.setCancelable(false);
		_prog_dlg.setCanceledOnTouchOutside(false);
		_prog_dlg.setMessage(msg);
		_prog_dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		_request_manager = manager;
	}

	public int get_delay_time() {
		return _delay_time;
	}

	public void set_delay_time(int _delay_time) {
		this._delay_time = _delay_time;
	}

	public void runProgressTask() {
		_prog_dlg.show();
		_is_delay_finish = false;
		_is_send_finish = false;
		_request_manager.addOnRequestChainCompleteNotify(new RequestChainCompleteAction());
		_request_manager.runRequestChain();
		(new TimeDelayAsyncTask()).execute(this._delay_time);
	}

	protected void checkFinish() {
		if (_is_send_finish && _is_delay_finish) {
			_prog_dlg.dismiss();
		}
	}

}
