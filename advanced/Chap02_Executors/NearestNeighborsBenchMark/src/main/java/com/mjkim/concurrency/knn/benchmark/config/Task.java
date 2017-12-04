package com.mjkim.concurrency.knn.benchmark.config;

import lombok.Data;

@Data
public class Task {
	private String title;
	private int threshold;
	private boolean parallel;
	private boolean group;
	private boolean sort;

}
