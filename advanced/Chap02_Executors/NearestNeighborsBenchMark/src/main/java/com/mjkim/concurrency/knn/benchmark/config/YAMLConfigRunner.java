package com.mjkim.concurrency.knn.benchmark.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.yaml.snakeyaml.Yaml;

public class YAMLConfigRunner {
	public Configuration load() throws IOException {
		Yaml yaml = new Yaml();
		try (InputStream in = Files.newInputStream(Paths.get("conf/application.yaml"))) {
			Configuration config = yaml.loadAs(in, Configuration.class);
			return config;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
