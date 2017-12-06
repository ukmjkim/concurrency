package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.net.Socket;

import com.mjkim.concurrency.advancedserver.concurrent.executor.ServerExecutor;

public class CommandFactory {
	public static Command getCommand(ServerExecutor executor, Socket clientSocket, String line) {
		String[] commandData = line.split(";");
		System.out.println("Command: " + commandData[0]);
		switch (commandData[0]) {
		case "s":
			System.out.println("Status");
			return new StatusCommand(executor, clientSocket, commandData);
		case "q":
			System.out.println("Query");
			return new QueryCommand(clientSocket, commandData);
		case "r":
			System.out.println("Report");
			return new ReportCommand(clientSocket, commandData);
		case "c":
			System.out.println("Cancel");
			return new CancelCommand(clientSocket, commandData);
		case "z":
			System.out.println("Stop");
			return new StopCommand(clientSocket, commandData);
		default:
			System.out.println("Error");
			return new ErrorCommand(clientSocket, commandData);
		}
	}
}
