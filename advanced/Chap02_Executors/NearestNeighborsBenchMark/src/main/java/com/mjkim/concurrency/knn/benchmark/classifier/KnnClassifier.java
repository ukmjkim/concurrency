package com.mjkim.concurrency.knn.benchmark.classifier;

import com.mjkim.concurrency.knn.benchmark.data.Entity;

public interface KnnClassifier {
	public String classify(Entity testingData) throws Exception;

	public void destroy();
}
