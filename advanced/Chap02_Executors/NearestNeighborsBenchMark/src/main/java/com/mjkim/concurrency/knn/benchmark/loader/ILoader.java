package com.mjkim.concurrency.knn.benchmark.loader;

import java.util.List;

import com.mjkim.concurrency.knn.benchmark.data.Entity;

public interface ILoader {
	public List<Entity> load(String dataSource);
}
