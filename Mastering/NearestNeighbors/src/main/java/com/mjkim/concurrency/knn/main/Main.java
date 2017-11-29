package com.mjkim.concurrency.knn.main;

import java.util.List;

import com.mjkim.concurrency.knn.classifier.KnnClassifier;
import com.mjkim.concurrency.knn.data.BankMarketing;
import com.mjkim.concurrency.knn.loader.BankMarketingLoader;

public class Main {
	public static void main(String... args) {
		if (args.length != 1) {
			System.out.println("[Usage] java com.mjkim.concurrency.knn.main.Main 100");
			System.exit(0);
		}
		System.out.println("K Nearest Neighbors Analyzing......");

		BankMarketingLoader loader = new BankMarketingLoader();
		List<BankMarketing> train = loader.load("data/bank.data");
		List<BankMarketing> test = loader.load("data/bank.test");

		int success = 0, mistakes = 0;
		int k = Integer.parseInt(args[0]);

		long start = System.currentTimeMillis();
		KnnClassifier classifier = new KnnClassifier(train, k);

		for (BankMarketing example : test) {
			String tag = classifier.classify(example);
			if (tag.equals(example.getTag())) {
				success++;
			} else {
				mistakes++;
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("******************************************");
		System.out.println("Serial Classifier - K: " + k);
		System.out.println("Success: " + success);
		System.out.println("Mistakes: " + mistakes);
		System.out.println("Execution Time: " + ((end - start) / 1000) + " seconds.");
		System.out.println("******************************************");
	}
}
