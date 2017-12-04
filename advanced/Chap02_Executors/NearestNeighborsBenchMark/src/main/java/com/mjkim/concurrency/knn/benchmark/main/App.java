package com.mjkim.concurrency.knn.benchmark.main;

import java.util.List;

import com.mjkim.concurrency.knn.benchmark.classifier.KnnClassifier;
import com.mjkim.concurrency.knn.benchmark.classifier.KnnClassifierFactory;
import com.mjkim.concurrency.knn.benchmark.config.Configuration;
import com.mjkim.concurrency.knn.benchmark.config.Task;
import com.mjkim.concurrency.knn.benchmark.config.YAMLConfigRunner;
import com.mjkim.concurrency.knn.benchmark.data.Entity;
import com.mjkim.concurrency.knn.benchmark.loader.BankMarketingTextLoader;
import com.mjkim.concurrency.knn.benchmark.loader.ILoader;

public class App {
	public static void main(String... args) throws Exception {
		Configuration config = new Configuration();
		try {
			config = (new YAMLConfigRunner()).load();
		} catch (Exception e) {
			System.exit(0);
		}
		List<Task> taskList = config.getTasks();

		ILoader loader = new BankMarketingTextLoader();
		List<Entity> train = loader.load("data/bank.data");
		List<Entity> test = loader.load("data/bank.test");

		System.out.printf("******************************************\n");
		System.out.printf("We are going to run %d tasks.\n", taskList.size());
		System.out.printf("Train: %d, Test: %d\n", train.size(), test.size());

		for (Task task : taskList) {
			process(task, train, test);
		}
	}

	public static void process(Task task, List<Entity> train, List<Entity> test) throws Exception {
		System.out.printf("******************************************\n");
		System.out.printf("K Nearest Neighbors Benchmark - %s\n", task.getTitle());
		System.out.printf("------------------------------------------\n");

		int success = 0;
		int mistakes = 0;

		long start = System.currentTimeMillis();
		KnnClassifier classifier = KnnClassifierFactory.getKnnClassifier(train, task);
		for (Entity testData : test) {
			String tag = classifier.classify(testData);
			if (tag.equals(testData.getTag())) {
				success++;
			} else {
				mistakes++;
			}
		}
		classifier.destroy();
		long end = System.currentTimeMillis();

		System.out.printf("Success: %d\n", success);
		System.out.printf("Mistakes: %d\n", mistakes);
		System.out.printf("Execution Time: %.2f seconds.\n", ((end - start) / 1000.0));
		System.out.printf("******************************************\n\n\n");
	}
}
