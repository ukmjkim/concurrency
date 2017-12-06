package com.mjkim.concurrency.advancedserver.parallel.log;

import java.util.concurrent.TimeUnit;

public class LogTask implements Runnable {
	@Override
	public void run() {
		try {
			while (true) {
				TimeUnit.SECONDS.sleep(10);
				Logger.writeLogs();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
