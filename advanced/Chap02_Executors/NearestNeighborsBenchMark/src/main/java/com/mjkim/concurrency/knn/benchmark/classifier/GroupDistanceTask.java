package com.mjkim.concurrency.knn.benchmark.classifier;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.mjkim.concurrency.knn.benchmark.data.Distance;
import com.mjkim.concurrency.knn.benchmark.data.Entity;
import com.mjkim.concurrency.knn.benchmark.distances.EuclideanDistancesCalculator;

public class GroupDistanceTask implements Runnable {
	private Distance[] distances;
	private int startIndex;
	private int endIndex;
	private List<Entity> dataSet;
	private Entity example;
	private CountDownLatch endController;

	public GroupDistanceTask(Distance[] distances, int startIndex, int endIndex, List<Entity> dataSet, Entity example,
			CountDownLatch endController) {
		this.distances = distances;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.dataSet = dataSet;
		this.example = example;
		this.endController = endController;
	}

	@Override
	public void run() {
		for (int index = startIndex; index < endIndex; index++) {
			Entity data = dataSet.get(index);
			distances[index] = new Distance();
			distances[index].setIndex(index);
			distances[index].setDistance(EuclideanDistancesCalculator.calculate(example, data));
		}
		endController.countDown();
	}
}
