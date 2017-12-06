package com.mjkim.concurrency.advancedserver.concurrent.executor;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mjkim.concurrency.advancedserver.concurrent.command.Command;
import com.mjkim.concurrency.advancedserver.concurrent.server.ConcurrentServer;
import com.mjkim.concurrency.advancedserver.parallel.log.Logger;

public class ServerExecutor extends ThreadPoolExecutor {
	private ConcurrentHashMap<Runnable, Date> startTimes;
	private ConcurrentHashMap<String, ExecutorStatistics> executionStatistics;
	private static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static long KEEP_ALIVE_TIME = 10;
	private static RejectedTaskController REJECTED_TASK_CONTROLLER = new RejectedTaskController();

	public ServerExecutor() {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new PriorityBlockingQueue<>(),
				REJECTED_TASK_CONTROLLER);
		startTimes = new ConcurrentHashMap<>();
		executionStatistics = new ConcurrentHashMap<String, ExecutorStatistics>();
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		ServerTask<?> task = (ServerTask<?>) r;
		Command command = task.getCommand();

		if (t == null) {
			if (!task.isCancelled()) {
				Date startDate = startTimes.remove(r);
				Date endDate = new Date();
				long executionTime = endDate.getTime() - startDate.getTime();
				ExecutorStatistics statistics = executionStatistics.computeIfAbsent(command.getUsername(),
						n -> new ExecutorStatistics());
				statistics.addExecutionTime(executionTime);
				statistics.addTask();
				ConcurrentServer.finishTask(command.getUsername(), command);
			} else {
				String message = "The task " + command.hashCode() + " of user " + command.getUsername()
						+ " has been cancelled.";
				Logger.sendMessage(message);
			}

		} else {
			String message = "The exception " + t.getMessage() + " has been thrown.";
			Logger.sendMessage(message);
		}
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		startTimes.put(r, new Date());
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		return new ServerTask<T>((Command) runnable);
	}

	public void writeStatistics() {
		for (Entry<String, ExecutorStatistics> entry : executionStatistics.entrySet()) {
			String user = entry.getKey();
			ExecutorStatistics stats = entry.getValue();
			Logger.sendMessage(user + ":" + stats);
		}

	}
}
