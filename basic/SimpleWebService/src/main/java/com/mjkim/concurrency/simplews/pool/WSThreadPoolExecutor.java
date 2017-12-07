package com.mjkim.concurrency.simplews.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class WSThreadPoolExecutor extends ThreadPoolExecutor {

	private final ThreadLocal startTime = new ThreadLocal();
	private final AtomicLong totalTime = new AtomicLong();
	private final AtomicLong numberOfTasks = new AtomicLong();

	public WSThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public WSThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public WSThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public WSThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		System.out.println("WSThreadPoolExecutor init....");

	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		System.out.println("Thread :" + t + " start " + r);
		startTime.set(System.nanoTime());
	}

	protected void afterExecution(Runnable r, Throwable t) {
		try {
			long endTime = System.nanoTime();
			long taskTime = endTime - (long) startTime.get();
			numberOfTasks.incrementAndGet();
			totalTime.addAndGet(taskTime);
			System.out.println("Thread :" + t + " end " + r + " , total time taken=" + taskTime);

		} finally {
			super.afterExecute(r, t);
		}
	}

	@Override
	protected void terminated() {
		try {
			System.out.println("Terminated: average time=" + (totalTime.get() / numberOfTasks.get()));
		} finally {
			super.terminated();
		}
	}
}