package com.mjkim.concurrency.advancedserver.parallel.cache;

import java.util.Date;

import lombok.Data;

@Data
public class CacheItem {
	private String command;
	private String response;
	private Date creationDate;
	private Date accessDate;

	public CacheItem(String command, String response) {
		creationDate=new Date();
		accessDate=new Date();
		this.command=command;
		this.response=response;
	}
}
