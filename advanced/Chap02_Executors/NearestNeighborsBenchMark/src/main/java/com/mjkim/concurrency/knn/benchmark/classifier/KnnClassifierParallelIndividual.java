package com.mjkim.concurrency.knn.benchmark.classifier;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.mjkim.concurrency.knn.benchmark.data.Distance;
import com.mjkim.concurrency.knn.benchmark.data.Entity;

public class KnnClassifierParallelIndividual implements KnnClassifier {
	private List<Entity> dataSet;
	private int k;
	private ThreadPoolExecutor executor;
	private int numThreads;
	private boolean parallelSort;

	public KnnClassifierParallelIndividual(List<Entity> dataSet, int k, int factor, boolean parallelSort) {
		this.dataSet = dataSet;
		this.k = k;
		numThreads = factor * (Runtime.getRuntime().availableProcessors());
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
		this.parallelSort = parallelSort;
	}

	@Override
	public String classify(Entity example) throws Exception {
		Distance[] distances = new Distance[dataSet.size()];
		CountDownLatch endController = new CountDownLatch(dataSet.size());

		int index = 0;
		for (Entity data : dataSet) {
			IndividualDistanceTask task = new IndividualDistanceTask(distances, index, data, example, endController);
			executor.execute(task);
			index++;
		}
		endController.await();

		if (parallelSort) {
			Arrays.parallelSort(distances);
		} else {
			Arrays.sort(distances);
		}

		Hashtable<String, Integer> results = new Hashtable<>();
		for (int i = 0; i < k; i++) {
			Entity data = dataSet.get(distances[i].getIndex());
			String tag = data.getTag();
			Integer counter = results.get(tag);
			if (counter == null) {
				counter = new Integer(1);
				results.put(tag, counter);
			} else {
				counter = counter++;
			}
		}

		Enumeration<String> keys = results.keys();
		int max = 0;
		String result = null;
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			int value = results.get(key);
			if (value > max) {
				max = value;
				result = key;
			}
		}

		return result;
	}

	@Override
	public void destroy() {
		executor.shutdown();
	}
}
