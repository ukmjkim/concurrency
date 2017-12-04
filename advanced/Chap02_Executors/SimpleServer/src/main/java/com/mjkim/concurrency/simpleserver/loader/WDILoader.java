package com.mjkim.concurrency.simpleserver.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mjkim.concurrency.simpleserver.data.WDI;

public class WDILoader {
	public List<WDI> load(String route) {
		Path file = Paths.get(route);
		List<WDI> dataSet = new ArrayList<>();
		try (InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line = null;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String data[] = parse(line);
				WDI dataObject = new WDI();
				dataObject.setData(data);
				dataSet.add(dataObject);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataSet;
	}

	private String[] parse(String line) {
		String[] ret = new String[59];
		int index = 0;
		StringBuffer buffer = new StringBuffer();
		boolean enComillas = false;
		for (int i = 0; i < line.length(); i++) {
			char letra = line.charAt(i);
			if (letra == '"') {
				enComillas = !enComillas;
			} else if ((letra == ',') && (!enComillas)) {
				ret[index] = buffer.toString();
				index++;
				buffer = new StringBuffer();
			} else {
				buffer.append(letra);
			}
		}
		ret[index] = buffer.toString();
		index++;
		return ret;
	}
}
