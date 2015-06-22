package com.mp.webservice.comm;


public class ServerResponse<ResType> {
	private String retcode = "";
	private String rescode = "";
	private String resmsg = "";
	private ResType resdata;
	
	public ServerResponse() {
	}

	public String get_retcode() {
		return retcode;
	}

	public void set_retcode(String retcode) {
		this.retcode = retcode;
	}

	public String get_rescode() {
		return rescode;
	}

	public void set_rescode(String rescode) {
		this.rescode = rescode;
	}

	public String get_resmsg() {
		return resmsg;
	}

	public void set_resmsg(String resmsg) {
		this.resmsg = resmsg;
	}

	public ResType get_resdata() {
		return resdata;
	}

	public void set_resdata(ResType resdata) {
		this.resdata = resdata;
	}

}
