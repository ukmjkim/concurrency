package com.mjkim.concurrency.simpleserver.parallel.cache;

import java.util.concurrent.TimeUnit;

import com.mjkim.concurrency.simpleserver.parallel.log.Logger;

public class CleanCacheTask implements Runnable {
	private ParallelCache cache;

	public CleanCacheTask(ParallelCache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				TimeUnit.SECONDS.sleep(10);
				Logger.sendMessage("Clean Cache Task started: " + Thread.currentThread().getName());
				cache.cleanCache();
			}
		} catch (InterruptedException e) {
			System.err.println("thread: " + Thread.currentThread().getName());
		}
	}
}
