package com.mp.webservice.comm.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PeterLi on 2015/6/12.
 */

public class CommRequestBitmapTask extends AsyncTask<String, Void, Bitmap> {

    private boolean _is_success = false;
    private CompleteCallBack _callback;

    public interface CompleteCallBack {
        void onGetBitmapComplete(boolean is_success, Bitmap bmp);
    }

    public CommRequestBitmapTask(CompleteCallBack callback) {
        super();
        this._callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bmp = null;
        _is_success = false;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            bmp = BitmapFactory.decodeStream(conn.getInputStream());
            _is_success = true;
        } catch (Exception e) {
            Log.w(this.getClass().getName(), e.toString());
        }

        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (null != _callback) {
            _callback.onGetBitmapComplete(_is_success, bitmap);
        }

    }

}
