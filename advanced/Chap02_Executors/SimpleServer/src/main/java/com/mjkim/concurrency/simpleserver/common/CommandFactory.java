package com.mjkim.concurrency.simpleserver.common;

public class CommandFactory {
	public static Command getCommand(String line) {
		String[] commandData = line.split(";");
		System.err.println("Command: " + commandData[0]);
		switch (commandData[0]) {
		case "s":
			System.err.println("Status");
			return new StatusCommand(commandData);
		case "q":
			System.err.println("Query");
			return new QueryCommand(commandData);
		case "r":
			System.err.println("Report");
			return new ReportCommand(commandData);
		case "z":
			System.err.println("Stop");
			return new StopCommand(commandData);
		default:
			System.err.println("Error");
			return new ErrorCommand(commandData);
		}
	}
}
