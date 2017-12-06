package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.net.Socket;

import com.mjkim.concurrency.advancedserver.concurrent.server.ConcurrentServer;
import com.mjkim.concurrency.advancedserver.parallel.log.Logger;

public class CancelCommand extends Command {
	public CancelCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	@Override
	public String execute() {
		ConcurrentServer.cancelTasks(getUsername());
		String message = "Tasks of user " + getUsername() + " has been cancelled.";
		Logger.sendMessage(message);
		return message;
	}
}
