package com.mjkim.concurrency.knn.benchmark.classifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mjkim.concurrency.knn.benchmark.data.Distance;
import com.mjkim.concurrency.knn.benchmark.data.Entity;
import com.mjkim.concurrency.knn.benchmark.distances.EuclideanDistancesCalculator;

public class KnnClassifierSerial implements KnnClassifier {
	private List<Entity> dataSet;
	private int k;

	public KnnClassifierSerial(List<Entity> dataSet, int k) {
		this.dataSet = dataSet;
		this.k = k;
	}

	@Override
	public String classify(Entity example) throws Exception {
		Distance[] distances = new Distance[dataSet.size()];

		int index = 0;
		for (Entity data : dataSet) {
			distances[index] = new Distance();
			distances[index].setIndex(index);
			distances[index].setDistance(EuclideanDistancesCalculator.calculate(example, data));
			index++;
		}
		Arrays.sort(distances);

		Map<String, Integer> results = new HashMap<>();
		for (int i = 0; i < k; i++) {
			Entity data = dataSet.get(distances[i].getIndex());
			String tag = data.getTag();
			results.merge(tag, 1, (a, b) -> a + b);
		}

		return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
	}

	@Override
	public void destroy() {

	}
}
