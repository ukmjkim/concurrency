package com.mjkim.concurrency.knn.benchmark.classifier;

import java.util.List;

import com.mjkim.concurrency.knn.benchmark.config.Task;
import com.mjkim.concurrency.knn.benchmark.data.Entity;

public class KnnClassifierFactory {
	public static KnnClassifier getKnnClassifier(List<Entity> dataSet, Task task) {
		if (task.isParallel()) {
			if (task.isGroup()) {
				return new KnnClassifierParallelGroup(dataSet, task.getThreshold(), 1, task.isSort());
			} else {
				return new KnnClassifierParallelIndividual(dataSet, task.getThreshold(), 1, task.isSort());
			}
		}

		return new KnnClassifierSerial(dataSet, task.getThreshold());
	}
}
