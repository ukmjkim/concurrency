package com.mjkim.concurrency.simpleserver.common;

public class ErrorCommand implements Command {
	protected String[] command;

	public ErrorCommand(String[] command) {
		this.command = command;
	}

	@Override
	public String execute() {
		return "Unknown command: " + command[0];
	}
}
