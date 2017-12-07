package com.mjkim.concurrency.simplews.pool;

import java.util.concurrent.atomic.AtomicInteger;

public class WSThread extends Thread {

	public static final String NAME = "WSThread";
	private static final AtomicInteger created = new AtomicInteger();

	public WSThread(Runnable r, String name) {
		super(r, name + "-" + created.incrementAndGet());
	}

	@Override
	public void run() {
		super.run();
	}

}