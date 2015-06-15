package com.mp.webservice.comm;


import java.util.ArrayList;
import java.util.List;

public class CommChainManager {

	private List<CommBaseRequest> _request_list;
	private OnRequestChainActionState _action_state = new SequenceState();
	private IRequestDataFinalAction _request_final_action = new IRequestDataFinalAction();
	private List<OnRequestChainComplete> _chain_complete_notify_list = new ArrayList<>();	// observer pattern
	private int _run_index = 0;

	public enum MODE {
		SEQUENCE, // Run requests one by one (Stop when running request failure)
		SEQUENCE_CONTINUE, // Run requests one by one (Non-Stop when running request failure)
		OVERALL	// Run all requests at the same time.
	}

	private interface OnRequestChainActionState {
		void onStartRunRequestChain(List<CommBaseRequest> list);
		void onRunSingleRequestComplete(int next_index, boolean is_success);
	}
	
	public interface OnRequestChainComplete {
		void onRequestChainComplete(boolean is_success);
	}
	
	class SequenceState implements OnRequestChainActionState {

		@Override
		public void onStartRunRequestChain(List<CommBaseRequest> list) {
			list.get(0).runSendData();
		}

		@Override
		public void onRunSingleRequestComplete(int next_index, boolean is_success) {
			if (!is_success) {
				runOnRequestChainCompleteNotify(is_success);
				return;
			}
			
			if (next_index < _request_list.size()) {
				_request_list.get(next_index).runSendData();
			} 
		}
	}
	
	class SequenceContinueState implements OnRequestChainActionState {
		
		@Override
		public void onStartRunRequestChain(List<CommBaseRequest> list) {
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
		public void onStartRunRequestChain(List<CommBaseRequest> list) {
			for (CommBaseRequest request : _request_list) request.runSendData();
		}
		
		@Override
		public void onRunSingleRequestComplete(int next_index, boolean is_success) {
		}
	}
	
	class IRequestDataFinalAction implements CommBaseRequest.IRequestDataFinalAction {

		private boolean _is_all_success = true;
		
		@Override
		public void onRequestFinally(boolean is_success) {
			_is_all_success = _is_all_success && is_success;

			++_run_index;
			
			if (_run_index >= _request_list.size()) {
				runOnRequestChainCompleteNotify(_is_all_success);
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
	
	public void addOnRequestChainCompleteNotify(OnRequestChainComplete notify) {
		_chain_complete_notify_list.add(notify);
	}

	protected void runOnRequestChainCompleteNotify(boolean is_success) {
		for (OnRequestChainComplete action : _chain_complete_notify_list) {
			action.onRequestChainComplete(is_success);
		}
	}
	
	public void addRequest(CommBaseRequest request) {
		if (null == _request_list) {
			_request_list = new ArrayList<>();
		}
		request.addCompleteNotify(_request_final_action);
		_request_list.add(request);
	}

	public void runRequestChain() {
		_run_index = 0;
		_request_final_action._is_all_success = true;
		_action_state.onStartRunRequestChain(_request_list);
	}

}
