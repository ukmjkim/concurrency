package com.mjkim.concurrency.knn.benchmark.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mjkim.concurrency.knn.benchmark.data.BankMarketing;
import com.mjkim.concurrency.knn.benchmark.data.Entity;

public class BankMarketingTextLoader implements ILoader {
	@Override
	public List<Entity> load(String dataSource) {
		Path file = Paths.get(dataSource);
		List<Entity> dataSet = new ArrayList<>();
		try (InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(";");
				BankMarketing dataObject = new BankMarketing();
				dataObject.setData(data);
				dataSet.add(dataObject);
			}
		} catch (IOException x) {
			x.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}
}
