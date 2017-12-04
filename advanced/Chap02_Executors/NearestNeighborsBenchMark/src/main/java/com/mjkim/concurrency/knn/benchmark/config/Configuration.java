package com.mjkim.concurrency.knn.benchmark.config;

import static java.lang.String.format;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public final class Configuration {
	private Date released;
	private String version;
	private List<String> protocols;
	private Map<String, String> users;
	private List<Task> tasks;

	@Override
	public String toString() {
		return new StringBuilder().append(format("Version: %s\n", version)).append(format("Released: %s\n", released))
				.append(format("Supported protocols: %s\n", protocols))
				.append(format("Users: %s\n", users)).toString();
	}
}
