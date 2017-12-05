package com.mjkim.concurrency.simpleserver.parallel.log;

import java.util.concurrent.TimeUnit;

/**
 * Task that writes all the messages in the log file every ten seconds. It
 * implements the Runnable interface. It will be executed as a thread
 * 
 * @author author
 *
 */
public class LogTask implements Runnable {
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				TimeUnit.SECONDS.sleep(10);
				Logger.sendMessage("LogTask started: " + Thread.currentThread().getName());
				Logger.writeLogs();
			}
		} catch (InterruptedException e) {
			System.out.println("thread: " + Thread.currentThread().getName());
		}
		Logger.writeLogs();
		System.out.println("LogTask finished: " + Thread.currentThread().getName());
	}
}
