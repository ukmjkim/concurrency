package com.mjkim.concurrency.simplews.pool;

import java.util.concurrent.ThreadFactory;

public class WSThreadFactory implements ThreadFactory {
	private final String poolName;

	public WSThreadFactory(String poolName) {
		this.poolName = poolName;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new WSThread(r, poolName);
	}
}
