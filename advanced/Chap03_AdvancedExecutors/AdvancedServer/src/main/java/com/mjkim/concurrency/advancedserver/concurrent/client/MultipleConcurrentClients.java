package com.mjkim.concurrency.advancedserver.concurrent.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mjkim.concurrency.advancedserver.common.Constants;

public class MultipleConcurrentClients {
	final static int NUM_CLIENTS = 5;

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		simulateClientRequests(executor);

		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		fetchStatusReport();
		cancelRequest();

		try {
			TimeUnit.MINUTES.sleep(2);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		stopAdvancedServer();

		executor.shutdown();
	}

	private static void simulateClientRequests(ThreadPoolExecutor executor) {
		for (int i = 1; i <= NUM_CLIENTS; i++) {
			System.out.println("Number of Simultaneous Clients: " + i);
			Thread[] threads = new Thread[i];
			for (int j = 0; j < i; j++) {
				String username = "USER_" + (j + 1);
				ConcurrentClient client = new ConcurrentClient(username, executor);
				threads[j] = new Thread(client);
				threads[j].start();
			}

			for (int j = 0; j < i; j++) {
				try {
					threads[j].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private static void fetchStatusReport() {
		try (Socket echoSocket = new Socket("localhost", Constants.CONCURRENT_PORT);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			StringWriter writer = new StringWriter();
			writer.write("s");
			writer.write(";");
			writer.write("USER_1");
			writer.write(";");
			writer.write(String.valueOf(10));
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void cancelRequest() {
		try (Socket echoSocket = new Socket("localhost", Constants.CONCURRENT_PORT);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			StringWriter writer = new StringWriter();
			writer.write("c");
			writer.write(";");
			writer.write("USER_2");
			writer.write(";");
			writer.write(String.valueOf(10));
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void stopAdvancedServer() {
		try (Socket echoSocket = new Socket("localhost", Constants.CONCURRENT_PORT);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			StringWriter writer = new StringWriter();
			writer.write("z");
			writer.write(";");
			writer.write("admin");
			writer.write(";");
			writer.write(String.valueOf(10));
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
