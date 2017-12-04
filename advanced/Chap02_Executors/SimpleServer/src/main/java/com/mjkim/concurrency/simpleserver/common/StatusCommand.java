package com.mjkim.concurrency.simpleserver.common;

import java.util.concurrent.ThreadPoolExecutor;

import com.mjkim.concurrency.simpleserver.concurrent.server.ConcurrentServer;
import com.mjkim.concurrency.simpleserver.parallel.log.Logger;

public class StatusCommand implements Command {
	protected String[] command;

	public StatusCommand(String[] command) {
		this.command = command;
	}

	@Override
	public String execute() {
		StringBuilder sb=new StringBuilder();
		ThreadPoolExecutor executor=ConcurrentServer.getExecutor();
		
		sb.append("Server Status;");
		sb.append("Actived Threads: ");
		sb.append(String.valueOf(executor.getActiveCount()));
		sb.append(";");
		sb.append("Maximum Pool Size: ");
		sb.append(String.valueOf(executor.getMaximumPoolSize()));
		sb.append(";");
		sb.append("Core Pool Size: ");
		sb.append(String.valueOf(executor.getCorePoolSize()));
		sb.append(";");
		sb.append("Pool Size: ");
		sb.append(String.valueOf(executor.getPoolSize()));
		sb.append(";");
		sb.append("Largest Pool Size: ");
		sb.append(String.valueOf(executor.getLargestPoolSize()));
		sb.append(";");
		sb.append("Completed Task Count: ");
		sb.append(String.valueOf(executor.getCompletedTaskCount()));
		sb.append(";");
		sb.append("Task Count: ");
		sb.append(String.valueOf(executor.getTaskCount()));
		sb.append(";");
		sb.append("Queue Size: ");
		sb.append(String.valueOf(executor.getQueue().size()));
		sb.append(";");
		sb.append("Cache Size: ");
		sb.append(String.valueOf(ConcurrentServer.getCache().getItemCount()));
		sb.append(";");
		Logger.sendMessage(sb.toString());
		return sb.toString();
	}

}
