package com.mjkim.concurrency.advancedserver.concurrent.executor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.mjkim.concurrency.advancedserver.concurrent.command.Command;

public class RejectedTaskController implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
		Command command = (Command) task;
		Socket clientSocket = command.getSocket();
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			String message = "The server is shutting down. Your request can not be served." + " Shuting Down: "
					+ String.valueOf(executor.isShutdown()) + ". Terminated: " + String.valueOf(executor.isTerminated())
					+ ". Terminating: " + String.valueOf(executor.isTerminating());
			out.println(message);
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
