package com.mjkim.concurrency.advancedserver.concurrent.client;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.mjkim.concurrency.advancedserver.wdi.data.WDI;
import com.mjkim.concurrency.advancedserver.wdi.data.WDIDAO;

public class ConcurrentClient implements Runnable {

	private String username;

	private ThreadPoolExecutor executor;

	public ConcurrentClient(String username, ThreadPoolExecutor executor) {
		this.username = username;
		this.executor = executor;
	}

	@Override
	public void run() {
		WDIDAO dao = WDIDAO.getDAO();
		List<WDI> data = dao.getData();
		Date globalStart, globalEnd;

		globalStart = new Date();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				QueryTask task = new QueryTask(data, username);
				executor.submit(task);
			}
			ReportTask task = new ReportTask(data, username);
			executor.submit(task);
		}

		globalEnd = new Date();
		System.out.println("Total Time: " + ((globalEnd.getTime() - globalStart.getTime()) / 1000) + " seconds.");

	}
}
