package com.demo.microservices.microservicecoursemanagement.model.response;

public class ResponseMsg {

	private String msg;
	private ResponseMsgType msgType;

	public ResponseMsg() {
		super();
	}
	
	public ResponseMsg(String msg, ResponseMsgType msgType) {
		super();
		this.msg = msg;
		this.msgType = msgType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ResponseMsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(ResponseMsgType msgType) {
		this.msgType = msgType;
	}

	@Override
	public String toString() {
		return String.format("{msg=%s, msgType=%s}",msg,msgType);
	}
}
