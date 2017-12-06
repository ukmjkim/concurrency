package com.mjkim.concurrency.advancedserver.parallel.log;

import static com.mjkim.concurrency.advancedserver.common.Constants.LOG_FILE;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {
	private static ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
	private static Thread thread;

	static {
		LogTask task = new LogTask();
		thread = new Thread(task);
		thread.start();
	}

	public static void sendMessage(String message) {
		StringWriter writer = new StringWriter();
		writer.write(new Date().toString());
		writer.write(": ");
		writer.write(message);
		logQueue.offer(writer.toString());
	}

	public static void writeLogs() {
		String message;
		Path file = Paths.get(LOG_FILE);
		try (BufferedWriter fileWriter = Files.newBufferedWriter(file, StandardOpenOption.CREATE,
				StandardOpenOption.APPEND)) {
			while ((message = logQueue.poll()) != null) {
				StringWriter writer = new StringWriter();
				writer.write(new Date().toString());
				writer.write(": ");
				writer.write(message);
				fileWriter.write(writer.toString());
				fileWriter.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initializeLog() {
		Path file = Paths.get(LOG_FILE);
		if (Files.exists(file)) {
			try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.TRUNCATE_EXISTING)) {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void shutdown() {
		writeLogs();
		thread.interrupt();
	}
}
