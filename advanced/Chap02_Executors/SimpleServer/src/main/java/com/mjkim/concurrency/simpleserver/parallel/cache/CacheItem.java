package com.mjkim.concurrency.simpleserver.parallel.cache;

import java.util.Date;

public class CacheItem {
	private String command;
	private String response;
	private Date creationDate;
	private Date accessDate;

	public CacheItem(String command, String response) {
		creationDate = new Date();
		accessDate = new Date();
		this.command = command;
		this.response = response;
	}

	public String getCommand() {
		return command;
	}

	public String getResponse() {
		return response;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
}
