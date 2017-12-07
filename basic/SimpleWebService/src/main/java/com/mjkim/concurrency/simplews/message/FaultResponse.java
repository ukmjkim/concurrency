package com.mjkim.concurrency.simplews.message;

public class FaultResponse extends Response {
	final static RESPONSE_TYPE responseType = RESPONSE_TYPE.FAULT;
	public FaultResponse() {
	}

	public FaultResponse(Exception e) {
		this.exception = e;
	}

	@Override
	public RESPONSE_TYPE getResponseType() {
		return responseType;
	}
}
