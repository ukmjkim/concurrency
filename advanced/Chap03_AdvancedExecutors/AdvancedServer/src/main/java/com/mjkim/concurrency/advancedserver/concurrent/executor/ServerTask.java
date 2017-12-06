package com.mjkim.concurrency.advancedserver.concurrent.executor;

import java.util.concurrent.FutureTask;

import com.mjkim.concurrency.advancedserver.concurrent.command.Command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerTask<V> extends FutureTask<V> implements Comparable<ServerTask<V>> {
	private Command command;
	
	public ServerTask(Command command) {
		super(command, null);
		this.command = command;
	}

	@Override
	public int compareTo(ServerTask<V> other) {
		return command.compareTo(other.getCommand());
	}
}
