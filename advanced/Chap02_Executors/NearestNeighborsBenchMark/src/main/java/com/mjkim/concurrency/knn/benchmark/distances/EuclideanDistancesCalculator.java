package com.mjkim.concurrency.knn.benchmark.distances;

import com.mjkim.concurrency.knn.benchmark.data.Entity;

public class EuclideanDistancesCalculator {
	public static double calculate(Entity entity1, Entity entity2) {
		double ret = 0.0d;

		double[] data1 = entity1.getScores();
		double[] data2 = entity2.getScores();

		if (data1.length != data2.length) {
			throw new IllegalArgumentException("Vector doesn't have the same length");
		}

		for (int i = 0; i < data1.length; i++) {
			ret += Math.pow(data1[i] - data2[i], 2);
		}
		return Math.sqrt(ret);
	}
}
