package com.mjkim.concurrency.simplews.broker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.mjkim.concurrency.simplews.message.DBQueryRequest;
import com.mjkim.concurrency.simplews.message.FaultResponse;
import com.mjkim.concurrency.simplews.message.Request;
import com.mjkim.concurrency.simplews.message.WS1Request;
import com.mjkim.concurrency.simplews.pool.WSThreadFactory;
import com.mjkim.concurrency.simplews.pool.WSThreadPoolExecutor;
import com.mjkim.concurrency.simplews.task.Task;

public class TaskManager {

	private static ThreadPoolExecutor webServiceExecutor;
	private static ThreadPoolExecutor dbExecutor;
	private static int CORE_POOL_SIZE = 10;
	private static int MAX_POOL_SIZE = 15;
	private static int KEEP_ALIVE_TIME = 10;
	private static int BLOCKING_QUEUE_CPACITY = 10;

	public static void main(String[] args) {
		List requestList = new ArrayList();
		requestList.add(new WS1Request("WS1Request_1"));
		requestList.add(new WS1Request("WS1Request_2"));
		requestList.add(new WS1Request("WS1Request_3"));
		requestList.add(new DBQueryRequest("DBQueryRequest_1"));
		requestList.add(new DBQueryRequest("DBQueryRequest_2"));
		requestList.add(new DBQueryRequest("DBQueryRequest_3"));

		Map responseMap = (new TaskManager()).processRequest(requestList);
	}

	void init() {
		webServiceExecutor = new WSThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new PriorityBlockingQueue(BLOCKING_QUEUE_CPACITY), new WSThreadFactory("WSThreadPool"));
		dbExecutor = new WSThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new PriorityBlockingQueue(BLOCKING_QUEUE_CPACITY), new WSThreadFactory("DBThreadPool"));
	}

	public Map processRequest(List requests) {
		if (requests == null || requests.isEmpty())
			throw new IllegalArgumentException("request is null or empty.");
		Iterator itr = requests.iterator();
		List taskList = new ArrayList();
		while (itr.hasNext()) {
			Request request = (Request) itr.next();
			Task task = request.createTask();
			taskList.add(task);
		}
		TaskManager parallelTaskManager = new TaskManager();
		parallelTaskManager.init();
		return parallelTaskManager.processInParallel(taskList);
	}

	Map processInParallel(List listOfTask) {
		Iterator taskItr = listOfTask.iterator();
		Map returnMap = Collections.synchronizedMap(new HashMap());
		Map responseMap = new HashMap();
		while (taskItr.hasNext()) {
			Task task = (Task) taskItr.next();
			Request req = task.getRequest();
			Request.REQUEST_TYPE rtype = req.getRequestType();
			ThreadPoolExecutor threadPoolExecutor = getExecutor(rtype);
			if (threadPoolExecutor == null)
				throw new RuntimeException("Could not get proper ThreadPoolExecutor, aborting the tasks..");
			// submit(task) will return Future
			returnMap.put(req, threadPoolExecutor.submit(task));
		}
		Request request;
		Future response;
		List requestListRequests = new ArrayList(returnMap.keySet());
		for (Object request1 : requestListRequests) {
			request = (Request) request1;
			response = (Future) returnMap.get(request);
			try {
				// get with timeout on Future
				responseMap.put(request, response.get(request.getTimeOutInSeconds(), TimeUnit.SECONDS));
			} catch (TimeoutException e) {
				createFault(responseMap, request, e);
			} catch (ExecutionException e) {
				createFault(responseMap, request, e);
			} catch (InterruptedException e) {
				createFault(responseMap, request, e);
			} catch (CancellationException e) {
				createFault(responseMap, request, e);
			} finally {
				response.cancel(true);
			}
		}
		return responseMap;
	}

	private void createFault(Map responseMap, Request request, Exception e) {
		FaultResponse faultResponse = new FaultResponse();
		faultResponse.setException(e);
		responseMap.put(request, faultResponse);
	}

	private ThreadPoolExecutor getExecutor(Request.REQUEST_TYPE requestType) {
		switch (requestType) {
		case DB_SIMPLE_QUERY:
			return dbExecutor;
		case WS1:
			return webServiceExecutor;
		case WS2:
			return dbExecutor;
		case DB_UPDATE:
			return dbExecutor;
		}
		return null;
	}

}
