package com.mjkim.concurrency.simpleserver.concurrent.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.mjkim.concurrency.simpleserver.common.Command;
import com.mjkim.concurrency.simpleserver.common.CommandFactory;
import com.mjkim.concurrency.simpleserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.simpleserver.parallel.log.Logger;

public class RequestTask implements Runnable {
	private Socket clientSocket;

	public RequestTask(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	@Override
	public void run() {
		try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String line = in.readLine();
			Logger.sendMessage(line);
			ParallelCache cache = ConcurrentServer.getCache();
			String response = cache.get(line);
			if (response == null) {
				Command command = CommandFactory.getCommand(line);
				response = command.execute();
			} else {
				Logger.sendMessage("Command " + line + " was found in the cache");
			}
			out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
