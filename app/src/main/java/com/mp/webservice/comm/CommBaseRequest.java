
package com.mp.webservice.comm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterLi on 2015/6/15.
 */
public abstract class CommBaseRequest {

    /**
     * A notification list.
     */
    private List<IRequestFinalAction> _final_action_list = new ArrayList<>();


    /**
     * A notification interface.
     */
    public interface IRequestFinalAction {

        /**
         * Recall method when request run int
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
    public void addCompleteNotify(IRequestFinalAction notify) {
        _final_action_list.add(notify);
    }

    /**
     * Clear all completion notify.
     */
    public void resetCompleteNotify() {
        _final_action_list.clear();
    }

    /**
     * Call the observer notify when this request is completed.
     * This method must be called manually when running action completion
     *
     * @param is_success an checking flag if this request executes successfully.
     */
    protected void runFinalAction(boolean is_success) {
        for (IRequestFinalAction notify : _final_action_list) {
            notify.onRequestComplete(is_success);
        }
    }

}
