package com.mjkim.concurrency.simplews.message;

public abstract class Response {
	public RESPONSE_TYPE responseType;
	protected Exception exception;

	public enum RESPONSE_TYPE {
		WS1, WS2, DB_UPDATE, DB_SIMPLE_QUERY, FAULT
	};

	public void setRequestType() {
		getResponseType();
	}

	public abstract RESPONSE_TYPE getResponseType();

	public Exception getException() {
		return exception;
	}

	public void setException(Exception e) {
		this.exception = e;
	}
}
