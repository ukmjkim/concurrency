package com.mjkim.concurrency.simplews.message;

import com.mjkim.concurrency.simplews.task.Task;

public abstract class Request {
	protected int timeOutInSeconds = Integer.MAX_VALUE;
	public REQUEST_TYPE requestType;

	public enum REQUEST_TYPE {
		WS1, WS2, DB_UPDATE, DB_SIMPLE_QUERY
	};
	
	public int getTimeOutInSeconds() {
		return timeOutInSeconds;
	}
	
	public void setTimeOutInSeconds(int timeOutInSeconds) {
		this.timeOutInSeconds = timeOutInSeconds;
	}
	
	public abstract REQUEST_TYPE getRequestType();
	public abstract Task createTask();
	@Override
	public abstract boolean equals(Object otherRequest);
	@Override
	public abstract int hashCode();
}
