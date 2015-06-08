package com.mp.webservice;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CommChainManager {

	private List<CommRequest<?, ?>> _request_list;
	private OnRequestChainActionState _action_state = new SequenceState();
	private OnRequestChainComplete _complete_action = new DefaultRequestChainComplete();
	private RequestDataFinalAction _request_final_action = new RequestDataFinalAction();
	private int _run_index = 0;

	public enum MODE {
		SEQUENCE, // Run requests one by one for chain sequence (Stop when running request failure)
		SEQUENCE_CONTINUE, // Run requests one by one for chain sequence (Non-Stop when running request failure)
		OVERALL	// Run all requests at the same time.
	}

	interface OnRequestChainActionState {
		public void onStartRunRequestChain(List<CommRequest<?, ?>> list);
		public void onRunSingleRequestComplete(int next_index, boolean is_success);
	}
	
	public interface OnRequestChainComplete {
		public void onRequestChainComplete(boolean is_success);
	}
	
	class DefaultRequestChainComplete implements OnRequestChainComplete {

		@Override
		public void onRequestChainComplete(boolean is_success) {
			Log.i(this.getClass().getName(), "Request Chain Complete");
		}
	}
	
	class SequenceState implements OnRequestChainActionState {

		@Override
		public void onStartRunRequestChain(List<CommRequest<?, ?>> list) {
			list.get(0).runSendData();
		}
		
		@Override
		public void onRunSingleRequestComplete(int next_index, boolean is_success) {
			if (!is_success) {
				_complete_action.onRequestChainComplete(is_success);
				return;
			}
			
			if (next_index < _request_list.size()) {
				_request_list.get(next_index).runSendData();
			} 
		}
	}
	
	class SequenceContinueState implements OnRequestChainActionState {
		
		@Override
		public void onStartRunRequestChain(List<CommRequest<?, ?>> list) {
			list.get(0).runSendData();
		}
		
		@Override
		public void onRunSingleRequestComplete(int next_index, boolean is_success) {
			if (next_index < _request_list.size()) {
				_request_list.get(next_index).runSendData();
			}
		}
	}
	
	class OverallState implements OnRequestChainActionState {

		@Override
		public void onStartRunRequestChain(List<CommRequest<?, ?>> list) {
			for (CommRequest<?, ?> request : _request_list) {
				request.runSendData();
			}
		}
		
		@Override
		public void onRunSingleRequestComplete(int next_index, boolean is_success) {
		}
	}
	
	class RequestDataFinalAction implements CommRequest.RequestDataFinalAction {

		private boolean _is_all_success = true;
		
		@Override
		public void onRequestFinally(boolean is_success) {
			_is_all_success = _is_all_success && is_success;

			++_run_index;
			
			if (_run_index >= _request_list.size()) {
				_complete_action.onRequestChainComplete(_is_all_success);
				return;
			}
			
			_action_state.onRunSingleRequestComplete(_run_index, is_success);
		}
		
		public void set_all_success_flag(boolean is_all) {
			_is_all_success = is_all;
		}
	}

	public CommChainManager() {

	}
	
	public void setMode(MODE mode) {
		switch (mode) {
		case SEQUENCE:
			_action_state = new SequenceState();
			break;
		case SEQUENCE_CONTINUE:
			_action_state = new SequenceContinueState();
			break;
		case OVERALL:
			_action_state = new OverallState();
			break;
		}
	}
	
	public void setOnRequestChainCompleteCallback(OnRequestChainComplete callback) {
		_complete_action = callback;
	}
	
	public void addRequest(CommRequest<?, ?> request) {
		if (null == _request_list) {
			_request_list = new ArrayList<CommRequest<?, ?>>();
		}
		request.setFinalAction(_request_final_action);
		_request_list.add(request);
	}

	public void runRequestChain() {
		_run_index = 0;
		_request_final_action._is_all_success = true;
		_action_state.onStartRunRequestChain(_request_list);
	}

	public void resetRequestChain() {
		_request_list.clear();
	}

}
