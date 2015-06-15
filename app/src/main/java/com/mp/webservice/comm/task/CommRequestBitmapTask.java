package com.mp.webservice.comm.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterLi on 2015/6/12.
 */

class RequestBitmapData {
    public Bitmap _bitmap;
    public String _msg;
}

public class CommRequestBitmapTask extends AsyncTask<String, Void, RequestBitmapData> {

    private boolean _is_success = false;
    private List<ActionComplete> _complete_notify = new ArrayList<>();

    public interface ActionComplete {
        void onGetBitmapComplete(boolean is_success, Bitmap bmp, String msg);
    }

    public CommRequestBitmapTask() {
        super();
    }

    public void addCompleteNotify(ActionComplete notify) {
        if (null == _complete_notify) {
            _complete_notify = new ArrayList<>();
        }
        _complete_notify.add(notify);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected RequestBitmapData doInBackground(String... params) {

        RequestBitmapData data = new RequestBitmapData();
        data._bitmap = null;
        data._msg = "";
        _is_success = false;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            data._bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            _is_success = true;
        } catch (Exception e) {
            Log.w(this.getClass().getName(), e.toString());
            data._msg = e.getMessage();
        }

        return data;
    }

    @Override
    protected void onPostExecute(RequestBitmapData data) {
        super.onPostExecute(data);
        for (ActionComplete notify : _complete_notify) {
            notify.onGetBitmapComplete(isSuccess(), data._bitmap, data._msg);
        }
    }

    public boolean isSuccess() {
        return _is_success;
    }

}
