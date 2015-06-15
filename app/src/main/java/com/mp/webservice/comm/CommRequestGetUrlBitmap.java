package com.mp.webservice.comm;

import android.graphics.Bitmap;

import com.mp.webservice.comm.task.CommRequestBitmapTask;

/**
 * Created by PeterLi on 2015/6/14.
 */
public class CommRequestGetUrlBitmap extends CommBaseRequest {

    private String _url_str = "";
    private IRequestGetUrlBitmapAction _complete_action = null;

    public interface IRequestGetUrlBitmapAction {
        void onSuccess(Bitmap bitmap);
        void onFailure(String msg);
    }

    class TaskActionComplete implements CommRequestBitmapTask.ActionComplete {

        @Override
        public void onGetBitmapComplete(boolean is_success, Bitmap bmp, String msg) {

            if (is_success) {
                _complete_action.onSuccess(bmp);
            } else {
                _complete_action.onFailure(msg);
            }

            runFinalAction(is_success); // must called finally
        }
    }

    public CommRequestGetUrlBitmap(String url_str, IRequestGetUrlBitmapAction action) {
        this._url_str = url_str;
        this._complete_action = action;
    }

    @Override
    public void runSendData() {
        CommRequestBitmapTask task = new CommRequestBitmapTask();
        task.addCompleteNotify(new TaskActionComplete());
        task.execute(_url_str);
    }
}
