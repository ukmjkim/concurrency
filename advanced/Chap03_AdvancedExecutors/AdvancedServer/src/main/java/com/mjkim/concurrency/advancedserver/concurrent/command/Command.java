package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Data;

import com.mjkim.concurrency.advancedserver.concurrent.server.ConcurrentServer;
import com.mjkim.concurrency.advancedserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.advancedserver.parallel.log.Logger;

@Data
public abstract class Command implements Comparable<Command>, Runnable {
	protected String[] command;
	private boolean cacheable;

	private String username;
	private byte priority;
	private Socket socket;

	public Command(String[] command) {
		this.command = command;
		cacheable = true;
	}

	public Command(Socket socket, String[] command) {
		this(command);
		username = command[1];
		priority = Byte.parseByte(command[2]);
		this.socket = socket;
	}

	public abstract String execute();

	@Override
	public void run() {
		String message = "Running a Task: Username: " + username + "; Priority: " + priority;
		Logger.sendMessage(message);

		String response = execute();
		ParallelCache cache = ConcurrentServer.getCache();
		
		if (isCacheable()) {
			cache.put(String.join(";",command), response);
		}
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(response);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response);
	}
	
	@Override
	public int compareTo(Command other) {
		return Byte.compare(other.getPriority(), this.getPriority());
	}
}
