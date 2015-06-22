
package com.mp.webservice.comm;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterLi on 2015/6/15.
 */
public abstract class CommBaseRequest {

    /**
     * A notification list.
     */
    private List<IRequestComplete> _final_action_list = new ArrayList<>();
    /**
     * A notification for CommChainManger class.
     */
    private IRequestComplete _req_chain_notify;

    /**
     * A notification interface.
     */
    public interface IRequestComplete {

        /**
         * Recall method when request run completion.
         *
         * @param is_success A flag show this request is success or failure.
         */
        void onRequestComplete(boolean is_success);
    }

    /**
     *  Start running the request action.
     */
    public abstract void runRequest();

    /**
     * Add notify object to observer list.
     *
     * @param notify A notify object
     */
    public void addCompleteNotify(IRequestComplete notify) {
        if (null == notify) {
            return;
        }
        _final_action_list.add(notify);
    }

    /**
     * Clear all completion notify.
     */
    public void resetCompleteNotify() {
        _final_action_list.clear();
    }

    /**
     * Set a completion notify for request chain.
     *
     * @param notify Request chain complete notify
     */
    void setRequestChainFinalNotify(IRequestComplete notify) {
        _req_chain_notify = notify;
    }

    /**
     * Call the observer notify when this request is completed.
     * This method must be called manually when running action completion
     *
     * @param is_success an checking flag if this request executes successfully.
     */
    protected void runCompleteAction(boolean is_success) {
        Log.i(getClass().getName(), "Run Complete Action");
        for (IRequestComplete notify : _final_action_list) {
            if (null != notify) {
                notify.onRequestComplete(is_success);
            }
        }

        if (null != (_req_chain_notify)) {
            _req_chain_notify.onRequestComplete(is_success);
        }
    }

}
