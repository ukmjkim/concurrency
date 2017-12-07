package com.mjkim.concurrency.simplews.task;

import java.util.ArrayList;
import java.util.List;

import com.mjkim.concurrency.simplews.exception.TaskExecutionException;
import com.mjkim.concurrency.simplews.message.Request;
import com.mjkim.concurrency.simplews.message.Response;
import com.mjkim.concurrency.simplews.message.WS1Request;
import com.mjkim.concurrency.simplews.message.WS1Response;

public class WS1Task extends Task {
	private WS1Request request;
	private String endPointURL;
	private String user;
	private String password;

	@Override
	public void setConnectionParam(String endPointURL, String user, String password) {
		this.endPointURL = endPointURL;
		this.user = user;
		this.password = password;
	}

	@Override
	public void setRequest(Request request) {
		this.request = (WS1Request) request;
	}

	@Override
	public Request getRequest() {
		return request;
	}

	@Override
	public Response call() throws TaskExecutionException {
		WS1Response response = new WS1Response();
		try {
			List all = new ArrayList();
			all.add("1234 Main St, Oakland CA 433322");
			all.add("1234 Washington Anv, San Francisco CA 3333");
			response.setAllAddresses(all);
			return response;
		} catch (Exception e) {
			TaskExecutionException taskExecutionException = new TaskExecutionException(e.getMessage());
			taskExecutionException.initCause(e);
			throw taskExecutionException;
		}
	}
}
