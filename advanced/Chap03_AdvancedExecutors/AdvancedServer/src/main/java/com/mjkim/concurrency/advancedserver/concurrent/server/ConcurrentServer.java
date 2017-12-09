package com.mjkim.concurrency.advancedserver.concurrent.server;

/*

@startuml
start
split
	repeat
		:Accepting a new client;
		:Adding the new client socket to queue;
		-[#black,dotted]->
		#HotPink:LinkedBlockingQueue<Socket>|
	repeat while (shutdown == false?)
split again
	fork
		:RequestTak(Runnable) Initialized;
		split
		fork
			:ServerExecutor Initialized;
			-[#black,dotted]->
			#SkyBlue:ThreadPoolService|
			repeat
				:beforeExecute();
				:Run Command();
				:afterExecute();
			repeat while (submitted?)
		end fork
		split again
			repeat
			#HotPink:LinkedBlockingQueue<Socket>|
			-[#black,dotted]->
			if (new socket?) then (yes)
				if (the request exists in cache?) then (yes)
					:Return cached data;
				else (no)
					:get a command from Factory;
					:Executor submit(command);
					-[#black,dotted]->
					#SkyBlue:ThreadPoolService|
					:add ServerTask(FutureTask) to taskController;
				endif
			else (no)
				:blocked;
			endif
			repeat while (shutdown == false?)
		end split
	end fork
split again
	:Logger Initialized;
	fork
		:LogTask;
			repeat
				:Writing logs;
				:sleep 10 secs;
			repeat while (shutdown == false?)
		stop
		end fork
split again
	:ParalleCache Initialized;
	fork
		:CleanCacheTask;
		repeat
			:Cleaning Cache;
			:sleep 10 secs;
		repeat while (shutdown == false?)
		stop
	end fork
end split
stop
@enduml

 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.mjkim.concurrency.advancedserver.common.Constants;
import com.mjkim.concurrency.advancedserver.concurrent.command.CancelCommand;
import com.mjkim.concurrency.advancedserver.concurrent.command.Command;
import com.mjkim.concurrency.advancedserver.concurrent.executor.ServerTask;
import com.mjkim.concurrency.advancedserver.parallel.cache.ParallelCache;
import com.mjkim.concurrency.advancedserver.parallel.log.Logger;

public class ConcurrentServer {
	private static ParallelCache cache;
	private static volatile boolean stopped = false;
	private static LinkedBlockingQueue<Socket> pendingConnections;
	private static ConcurrentMap<String, ConcurrentMap<Command, ServerTask<?>>> taskController;
	private static Thread requestTaskThread;
	private static RequestTask requestTask;
	private static ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		cache = new ParallelCache();
		Logger.initializeLog();
		pendingConnections = new LinkedBlockingQueue<Socket>();
		taskController = new ConcurrentHashMap<String, ConcurrentMap<Command, ServerTask<?>>>();
		requestTask = new RequestTask(pendingConnections, taskController);
		requestTaskThread = new Thread(requestTask);
		requestTaskThread.start();

		System.out.println("Initialization completed.");

		serverSocket = new ServerSocket(Constants.CONCURRENT_PORT);
		do {
			try {
				Socket clientSocket = serverSocket.accept();
				pendingConnections.put(clientSocket);
			} catch (IOException e) {
				// No action required
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!stopped);
		finishServer();
		System.out.println("Shutting down cache");
		cache.shutdown();
		System.out.println("Cache ok" + new Date());
	}

	public static ParallelCache getCache() {
		return cache;
	}

	private static void finishServer() {
		System.out.println("Shutting down the server...");
		requestTask.shutdown();
		requestTask.terminate();
		System.out.println("Shutting down Request task");
		requestTaskThread.interrupt();
		System.out.println("Request task ok");
		System.out.println("Closing socket");
		System.out.println("Shutting down logger");
		Logger.sendMessage("Shuttingdown the logger");
		Logger.shutdown();
		System.out.println("Logger ok");
		System.out.println("Main server thread ended");
	}

	public static void shutdown() {
		stopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void cancelTasks(String username) {

		ConcurrentMap<Command, ServerTask<?>> userTasks = taskController.get(username);
		if (userTasks == null) {
			return;
		}
		int taskNumber = 0;
		Iterator<ServerTask<?>> it = userTasks.values().iterator();
		while (it.hasNext()) {
			ServerTask<?> task = it.next();
			Command command = task.getCommand();
			if (!(command instanceof CancelCommand) && task.cancel(true)) {
				taskNumber++;
				Logger.sendMessage(
						"Task with hashCode " + command.hashCode() + "(username:" + command.getUsername()
								+ ", priority: " + command.getPriority() + ") has cancelled: "
								+ command.getClass().getSimpleName());
				it.remove();
			}
		}
		String message = taskNumber + " tasks has been cancelled.";
		Logger.sendMessage(message);

	}

	public static void finishTask(String username, Command command) {

		ConcurrentMap<Command, ServerTask<?>> userTasks = taskController.get(username);

		userTasks.remove(command);

		String message = "Task with hashCode " + command.hashCode() + "(username:" + command.getUsername()
				+ ", priority: " + command.getPriority() + ") has finished";
		Logger.sendMessage(message);

	}
}
