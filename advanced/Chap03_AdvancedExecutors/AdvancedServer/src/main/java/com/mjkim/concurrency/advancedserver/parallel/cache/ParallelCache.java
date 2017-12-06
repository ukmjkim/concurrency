package com.mjkim.concurrency.advancedserver.parallel.cache;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelCache {
	private ConcurrentHashMap<String, CacheItem> cache;
	private CleanCacheTask task;
	private Thread thread;

	public ParallelCache() {
		cache = new ConcurrentHashMap<String, CacheItem>();
		task = new CleanCacheTask(this);
		thread = new Thread(task);
		thread.start();
	}

	public void put(String command, String response) {
		CacheItem item = new CacheItem(command, response);
		cache.put(command, item);
	}

	public String get(String command) {
		CacheItem item = cache.get(command);
		if (item == null) {
			return null;
		}
		item.setAccessDate(new Date());
		return item.getResponse();
	}

	public void cleanCache() {
		Enumeration<String> keys = cache.keys();
		Date revisionDate = new Date();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			CacheItem item = cache.get(key);
			if (revisionDate.getTime() - item.getAccessDate().getTime() > 600000) {
				cache.remove(key);
			}
		}
	}

	public void shutdown() {
		thread.interrupt();
	}

	public int getItemNumber() {
		return cache.size();
	}
}
