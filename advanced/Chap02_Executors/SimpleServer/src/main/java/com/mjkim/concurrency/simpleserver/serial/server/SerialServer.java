package com.mjkim.concurrency.simpleserver.serial.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.mjkim.concurrency.simpleserver.common.Command;
import com.mjkim.concurrency.simpleserver.common.CommandFactory;
import com.mjkim.concurrency.simpleserver.common.Constants;
import com.mjkim.concurrency.simpleserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.simpleserver.parallel.log.Logger;

public class SerialServer {
	private static boolean stopped = false;

	public static void main(String[] args) throws IOException {
		ParallelCache cache = new ParallelCache();
		Logger.initializeLog();

		System.out.println("Initialization completed.");
		Logger.sendMessage("Initialization completed.");

		try (ServerSocket serverSocket = new ServerSocket(Constants.SERIAL_PORT)) {
			do {
				System.out.println("Waiting a connection from a client...");
				Logger.sendMessage("Waiting a connection from a client...");
				try (Socket clientSocket = serverSocket.accept();
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
					String line = in.readLine();

					// Logger.sendMessage(line);
					String response = cache.get(line);
					if (response == null) {
						Command command = CommandFactory.getCommand(line);
						response = command.execute();
					} else {
						Logger.sendMessage("Command " + line + " was found in the cache");
					}
					// Logger.sendMessage(response);
					out.println(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (!stopped);
			serverSocket.close();
			System.out.println("Closing socket");
		}
		System.out.println("Stopping...");
		System.exit(0);
	}

	public static void shutdown() {
		stopped = true;
		System.out.println("Shuttingdown the logger");
		Logger.sendMessage("Shuttingdown the logger");
		Logger.shutdown();
	}
}
