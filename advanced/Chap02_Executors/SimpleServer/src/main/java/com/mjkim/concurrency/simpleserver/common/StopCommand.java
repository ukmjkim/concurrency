package com.mjkim.concurrency.simpleserver.common;

import com.mjkim.concurrency.simpleserver.serial.server.SerialServer;

public class StopCommand implements Command {
	protected String[] command;

	public StopCommand(String[] command) {
		this.command = command;
	}

	@Override
	public String execute() {
		SerialServer.shutdown();
		return "Server stopped";
	}
}
