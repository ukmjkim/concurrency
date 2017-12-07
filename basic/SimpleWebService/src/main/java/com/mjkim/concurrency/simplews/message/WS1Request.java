package com.mjkim.concurrency.simplews.message;

import com.mjkim.concurrency.simplews.task.Task;
import com.mjkim.concurrency.simplews.task.WS1Task;

public class WS1Request extends Request {
	final static REQUEST_TYPE requestType = REQUEST_TYPE.WS1;
	private String requestId;

	public WS1Request(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public REQUEST_TYPE getRequestType() {
		return requestType;
	}

	@Override
	public Task createTask() {
		WS1Task task = new WS1Task();
		task.setRequest(this);
		return task;
	}

	@Override
	public boolean equals(Object otherRequest) {
		if (otherRequest == this)
			return true;
		if (otherRequest == null || !otherRequest.getClass().equals(this.getClass()))
			return false;
		String otherRequestId = (String) otherRequest;
		return otherRequestId.equals(requestId);
	}

	@Override
	public int hashCode() {
		int result = 7;
		int requestIdI = requestId.hashCode();
		result = 37 * result + requestIdI;
		return result;
	}
}
