package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.net.Socket;

public class ErrorCommand extends Command {
	public ErrorCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	@Override
	public String execute() {
		return "Unknown command: " + command[0];
	}
}
