package com.mjkim.concurrency.simpleserver.concurrent.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mjkim.concurrency.simpleserver.common.Constants;
import com.mjkim.concurrency.simpleserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.simpleserver.parallel.log.Logger;

public class ConcurrentServer {
	private static volatile boolean stopped = false;
	private static ThreadPoolExecutor executor;
	private static ParallelCache cache;

	public static void main(String[] args) throws IOException, InterruptedException {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		cache = new ParallelCache();
		Logger.initializeLog();

		System.out.println("Initialization completed.");
		Logger.sendMessage("Initialization completed.");

		try (ServerSocket serverSocket = new ServerSocket(Constants.CONCURRENT_PORT)) {
			do {
				System.out.println("Waiting a connection from a client...");
				Logger.sendMessage("Waiting a connection from a client...");
				try {
					Socket clientSocket = serverSocket.accept();
					RequestTask task = new RequestTask(clientSocket);
					executor.execute(task);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (!stopped);

			executor.awaitTermination(1, TimeUnit.DAYS);

			System.out.println("Shutting down cache");
			cache.shutdown();
			System.out.println("Cache ok");

			serverSocket.close();
			System.out.println("Socket ok");
			System.out.println("Main server thread ended");
		}
	}

	public static ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public static ParallelCache getCache() {
		return cache;
	}

	public static void shutdown() {
		stopped = true;
		System.out.println("Shutting down the server...");
		System.out.println("Shutting down executor");
		executor.shutdown();
		System.out.println("Executor ok");
		System.out.println("Shutting down logger");
		Logger.sendMessage("Shuttingdown the logger");
		Logger.shutdown();
		System.out.println("Logger ok");
	}
}
