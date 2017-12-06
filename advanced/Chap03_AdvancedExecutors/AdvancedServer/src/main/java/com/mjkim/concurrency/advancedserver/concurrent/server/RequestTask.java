package com.mjkim.concurrency.advancedserver.concurrent.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mjkim.concurrency.advancedserver.concurrent.command.Command;
import com.mjkim.concurrency.advancedserver.concurrent.command.CommandFactory;
import com.mjkim.concurrency.advancedserver.concurrent.executor.ServerExecutor;
import com.mjkim.concurrency.advancedserver.concurrent.executor.ServerTask;
import com.mjkim.concurrency.advancedserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.advancedserver.parallel.log.Logger;

public class RequestTask implements Runnable {
	private ServerExecutor executor = new ServerExecutor();
	private LinkedBlockingQueue<Socket> pendingConnections;
	private ConcurrentMap<String, ConcurrentMap<Command, ServerTask<?>>> taskController;

	public RequestTask(LinkedBlockingQueue<Socket> pendingConnections,
			ConcurrentMap<String, ConcurrentMap<Command, ServerTask<?>>> taskController) {
		this.pendingConnections = pendingConnections;
		this.taskController = taskController;
	}

	@Override
	public void run() {

		try {
			while (!Thread.currentThread().interrupted()) {
				try {
					Socket clientSocket = pendingConnections.take();
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String line = in.readLine();

					Logger.sendMessage(line);

					ParallelCache cache = ConcurrentServer.getCache();
					String ret = cache.get(line);
					if (ret == null) {
						String[] commandData = line.split(";");
						System.out.println("Command: " + commandData[0]);

						Command command = CommandFactory.getCommand(executor, clientSocket, line);

						ServerTask<?> controller = (ServerTask<?>) executor.submit(command);
						storeController(command.getUsername(), controller, command);
					} else {
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						out.println(ret);
						clientSocket.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			// No Action Required
		}
	}

	private void storeController(String userName, ServerTask<?> controller, Command command) {
		taskController.computeIfAbsent(userName, k -> new ConcurrentHashMap<>()).put(command, controller);
	}

	public void shutdown() {
		String message = "Request Task: " + pendingConnections.size() + " pending connections.";
		Logger.sendMessage(message);
		executor.shutdown();
	}

	public void terminate() {
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
			executor.writeStatistics();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public ServerExecutor getExecutor() {
		return executor;
	}
}
