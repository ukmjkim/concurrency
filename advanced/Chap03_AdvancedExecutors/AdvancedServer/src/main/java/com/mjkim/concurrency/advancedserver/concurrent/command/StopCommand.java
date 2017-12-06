package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.net.Socket;

import com.mjkim.concurrency.advancedserver.concurrent.server.ConcurrentServer;

public class StopCommand extends Command {
	public StopCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	@Override
	public String execute() {
		ConcurrentServer.shutdown();
		return "Server stopped";
	}
}
