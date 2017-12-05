package com.mjkim.concurrency.simpleserver.common;

import com.mjkim.concurrency.simpleserver.concurrent.server.ConcurrentServer;

public class StopCommand implements Command {
	protected String[] command;

	public StopCommand(String[] command) {
		this.command = command;
	}

	@Override
	public String execute() {
		ConcurrentServer.shutdown();
		return "Server stopped";
	}
}
