package com.mjkim.concurrency.knn.classifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mjkim.concurrency.knn.data.DataSet;
import com.mjkim.concurrency.knn.data.Distance;
import com.mjkim.concurrency.knn.distances.EuclideanDistanceCalculator;

public class KnnClassifier {
	private List<? extends DataSet> dataSet;
	private int k;

	public KnnClassifier(List<? extends DataSet> dataSet, int k) {
		this.dataSet = dataSet;
		this.k = k;

	}
	public String classify(DataSet example) {
		Distance[] distances = new Distance[dataSet.size()];
		int index = 0;

		for (DataSet localExample : dataSet) {
			distances[index] = new Distance();
			distances[index].setIndex(index);
			distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
			index++;
		}
		Arrays.sort(distances);

		Map<String, Integer> results = new HashMap<>();
		for (int i = 0; i < k; i++) {
			DataSet localExample = dataSet.get(distances[i].getIndex());
			String tag = localExample.getTag();
			results.merge(tag, 1, (a, b) -> a + b);
		}

		System.out.println(results);

		return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
	}
}
