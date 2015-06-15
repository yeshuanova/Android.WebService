package com.mp.webservice.comm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterLi on 2015/6/15.
 */
public abstract class CommBaseRequest {

    private List<IRequestDataFinalAction> _final_action_list = new ArrayList<>();

    public abstract void runSendData();

    public void addCompleteNotify(IRequestDataFinalAction notify) {
        _final_action_list.add(notify);
    }

    /**
     * Call the observer notify when this request is completed.
     * This method must be called manually when running action completion
     *
     * @param is_success an checking flag if this request executes successfully.
     */
    protected void runFinalAction(boolean is_success) {
        for (IRequestDataFinalAction notify : _final_action_list) {
            notify.onRequestFinally(is_success);
        }
    }

    public interface IRequestDataFinalAction {
        void onRequestFinally(boolean is_success);
    }
}
