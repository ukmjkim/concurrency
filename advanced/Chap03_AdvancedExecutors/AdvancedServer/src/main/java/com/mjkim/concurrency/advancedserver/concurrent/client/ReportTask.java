package com.mjkim.concurrency.advancedserver.concurrent.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import com.mjkim.concurrency.advancedserver.common.Constants;
import com.mjkim.concurrency.advancedserver.wdi.data.WDI;

public class ReportTask implements Runnable {

	private List<WDI> data;
	private String username;

	public ReportTask(List<WDI> data, String username) {
		this.data = data;
		this.username = username;
	}

	@Override
	public void run() {
		Random randomGenerator = new Random();

		try (Socket echoSocket = new Socket("localhost", Constants.CONCURRENT_PORT);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
			WDI wdi = data.get(randomGenerator.nextInt(data.size()));

			StringWriter writer = new StringWriter();
			writer.write("r");
			writer.write(";");
			writer.write(username);
			writer.write(";");
			writer.write(String.valueOf(10));
			writer.write(";");
			writer.write(wdi.getIndicatorCode());

			String command = writer.toString();
			out.println(command);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
