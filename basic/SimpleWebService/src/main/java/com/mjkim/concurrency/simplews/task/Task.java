package com.mjkim.concurrency.simplews.task;

import java.util.concurrent.Callable;

import com.mjkim.concurrency.simplews.message.Request;
import com.mjkim.concurrency.simplews.message.Response;

public abstract class Task implements Callable<Response> {
	public abstract void setConnectionParam(String connUrl, String userName, String password);

	public abstract void setRequest(Request request);

	public abstract Request getRequest();

	@Override
	public abstract Response call() throws Exception;
}
