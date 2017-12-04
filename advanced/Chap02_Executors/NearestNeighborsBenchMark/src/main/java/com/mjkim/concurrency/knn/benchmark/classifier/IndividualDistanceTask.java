package com.mjkim.concurrency.knn.benchmark.classifier;

import java.util.concurrent.CountDownLatch;

import com.mjkim.concurrency.knn.benchmark.data.Distance;
import com.mjkim.concurrency.knn.benchmark.data.Entity;
import com.mjkim.concurrency.knn.benchmark.distances.EuclideanDistancesCalculator;

public class IndividualDistanceTask implements Runnable {
	private Distance[] distances;
	private int index;
	private Entity data;
	private Entity example;
	private CountDownLatch endController;

	public IndividualDistanceTask(Distance[] distances, int index, Entity data, Entity example,
			CountDownLatch endController) {
		this.distances = distances;
		this.index = index;
		this.data = data;
		this.example = example;
		this.endController = endController;
	}

	@Override
	public void run() {
		distances[index] = new Distance();
		distances[index].setIndex(index);
		distances[index].setDistance(EuclideanDistancesCalculator.calculate(example, data));
		endController.countDown();
	}
}
