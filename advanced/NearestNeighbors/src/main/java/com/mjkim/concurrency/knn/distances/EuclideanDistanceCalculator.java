package com.mjkim.concurrency.knn.distances;

import com.mjkim.concurrency.knn.data.DataSet;

public class EuclideanDistanceCalculator {
	public static double calculate(DataSet example1, DataSet example2) {
		double ret = 0.0d;

		double[] data1 = example1.getScores();
		double[] data2 = example2.getScores();

		if (data1.length != data2.length) {
			throw new IllegalArgumentException("Vector doesn't have the same length");
		}

		for (int i = 0; i < data1.length; i++) {
			ret += Math.pow(data1[i] - data2[i], 2);
		}
		return Math.sqrt(ret);
	}
}
