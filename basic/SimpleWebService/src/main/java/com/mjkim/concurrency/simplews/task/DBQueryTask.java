package com.mjkim.concurrency.simplews.task;

import java.util.ArrayList;
import java.util.List;

import com.mjkim.concurrency.simplews.exception.TaskExecutionException;
import com.mjkim.concurrency.simplews.message.DBQueryRequest;
import com.mjkim.concurrency.simplews.message.DBQueryResponse;
import com.mjkim.concurrency.simplews.message.Request;
import com.mjkim.concurrency.simplews.message.Response;

public class DBQueryTask extends Task {
	private DBQueryRequest request;
	private String dbURL;
	private String user;
	private String password;

	@Override
	public void setConnectionParam(String dbURL, String user, String password) {
		this.dbURL = dbURL;
		this.user = user;
		this.password = password;
	}

	@Override
	public void setRequest(Request request) {
		this.request = (DBQueryRequest) request;
	}

	@Override
	public Request getRequest() {
		return request;
	}

	@Override
	public Response call() throws TaskExecutionException {
		DBQueryResponse response = new DBQueryResponse();
		try {
			List all = new ArrayList();
			all.add("1234 Main St, Oakland CA 433322");
			all.add("1234 Wasington Ave, San Francisco CA 3333");
			all.add("8765 NotA Ave, City QQ 77777");
			response.setAllAddresses(all);
			return response;
		} catch (Exception e) {
			TaskExecutionException taskExecutionException = new TaskExecutionException(e.getMessage());
			taskExecutionException.initCause(e);
			throw taskExecutionException;
		}
	}
}
