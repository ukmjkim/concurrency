package com.mjkim.concurrency.knn.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mjkim.concurrency.knn.data.BankMarketing;

public class BankMarketingLoader {
	public List<BankMarketing> load(String dataPath) {
		List<BankMarketing> dataSet = new ArrayList<BankMarketing>();
		Path filePath = Paths.get(dataPath);
		try (InputStream in = Files.newInputStream(filePath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(";");
				BankMarketing dataObject = new BankMarketing();
				dataObject.setData(data);
				dataSet.add(dataObject);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataSet;
	}
}
